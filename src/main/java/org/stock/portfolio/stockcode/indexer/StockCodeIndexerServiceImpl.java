package org.stock.portfolio.stockcode.indexer;

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
import org.stock.portfolio.commons.indexer.TotalCountDto;
import org.stock.portfolio.stockcode.StockCodeIndexerService;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class StockCodeIndexerServiceImpl implements StockCodeIndexerService {

    @Value("${suggestions.results.size}")
    private int suggestionsResultsSize;

    @Autowired
    private Client client;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private StockCodeIndexRepository stockCodeIndexRepository;

    @Override
    public FacetedPage<StockCodeDto> listAllStockCodes(int page, int size) {
        QueryBuilder builder = QueryBuilders.matchAllQuery();
        Pageable pageable = new PageRequest(page, size);
        return stockCodeIndexRepository.search(builder, pageable);
    }

    @Override
    public TotalCountDto countStockCodes() {
        CountResponse response = client.prepareCount(StockCodeDto.INDEX_NAME)
                .execute()
                .actionGet();
        return new TotalCountDto(response.getCount());
    }



    @Override
    public List<StockCodeDto> suggestStockCodes(String term) {
        List<Suggest.Suggestion.Entry.Option> suggestions = suggest(term , StockCodeDto.INDEX_NAME, "stock-code");
        return  suggestions
                .stream()
                .map(option -> StockCodeDto.fromOption(mapper, option))
                .collect(toList());
    }


    // TODO: Refactor 'suggest' field. It is not safe to leave it as string.
    private List<Suggest.Suggestion.Entry.Option> suggest(String term, String indexName, String completionName) {
        CompletionSuggestionBuilder builder = new CompletionSuggestionBuilder(completionName)
                .field("suggest")
                .text(term)
                .size(suggestionsResultsSize);

        Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> suggestions =
                client.prepareSuggest(indexName)
                        .addSuggestion(builder)
                        .execute()
                        .actionGet()
                        .getSuggest()
                        .getSuggestion(completionName);

        List<Suggest.Suggestion.Entry.Option> options = new ArrayList<>();
        for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> next : suggestions) {
            options.addAll(next.getOptions());
        }

        return options;
    }
}
