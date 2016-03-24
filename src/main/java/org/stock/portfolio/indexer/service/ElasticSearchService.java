package org.stock.portfolio.indexer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.stereotype.Service;
import org.stock.portfolio.indexer.IndexerService;
import org.stock.portfolio.indexer.StockCodeIndex;
import org.stock.portfolio.indexer.dto.StockCodeDto;
import org.stock.portfolio.indexer.dto.StockCodeHistoryEntryDto;
import org.stock.portfolio.indexer.dto.TotalCountDto;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.elasticsearch.search.suggest.Suggest.Suggestion.Entry;
import static org.elasticsearch.search.suggest.Suggest.Suggestion.Entry.Option;

@Service
public class ElasticSearchService implements IndexerService {

    @Value("${suggestions.results.size}")
    private int suggestionsResultsSize;

    @Autowired
    private Client client;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private StockCodeIndex stockCodeIndex;

    @Override
    public FacetedPage<StockCodeDto> listAllStockCodes(int page, int size) {
        QueryBuilder builder = QueryBuilders.matchAllQuery();
        Pageable pageable = new PageRequest(page, size);
        return stockCodeIndex.search(builder, pageable);
    }

    @Override
    public TotalCountDto countStockCodes() {
        CountResponse response = client.prepareCount(StockCodeDto.INDEX_NAME)
                .execute()
                .actionGet();
        return new TotalCountDto(response.getCount());
    }

    @Override
    public TotalCountDto countStockCodeHistoryEntries() {
        CountResponse response = client.prepareCount(StockCodeHistoryEntryDto.INDEX_NAME)
                .execute()
                .actionGet();
        return new TotalCountDto(response.getCount());
    }

    @Override
    public List<StockCodeDto> suggestStockCodes(String term) {
        List<Entry.Option> suggestions = suggest(term , StockCodeDto.INDEX_NAME, "stock-code");
        return  suggestions
                .stream()
                .map(option -> StockCodeDto.fromOption(mapper, option))
                .collect(toList());
    }

    @Override
    public List<StockCodeHistoryEntryDto> suggestStockCodeHistory(String term) {
        List<Entry.Option> suggestions = suggest(term, StockCodeHistoryEntryDto.INDEX_NAME, "stock-code-history");
        return suggestions
                .stream()
                .map(option -> StockCodeHistoryEntryDto.fromOption(mapper, option))
                .collect(toList());
    }



    // TODO: Refactor 'suggest' field. It is not safe to leave it as string.
    private List<Entry.Option> suggest(String term, String indexName, String completionName) {
        CompletionSuggestionBuilder builder = new CompletionSuggestionBuilder(completionName)
                .field("suggest")
                .text(term)
                .size(suggestionsResultsSize);

        Suggest.Suggestion<? extends Entry<? extends Option>> suggestions =
                client.prepareSuggest(indexName)
                .addSuggestion(builder)
                .execute()
                .actionGet()
                .getSuggest()
                .getSuggestion(completionName);

        List<Entry.Option> options = new ArrayList<>();
        for (Entry<? extends Entry.Option> next : suggestions) {
            options.addAll(next.getOptions());
        }

        return options;
    }
}
