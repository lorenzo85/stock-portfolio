package org.stock.portfolio.stockcodehistory.indexer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.stock.portfolio.commons.indexer.TotalCountDto;
import org.stock.portfolio.stockcodehistory.StockCodeHistoryIndexerService;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class StockCodeHistoryIndexerServiceImpl implements StockCodeHistoryIndexerService {

    @Value("${suggestions.results.size}")
    private int suggestionsResultsSize;

    @Autowired
    private Client client;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public TotalCountDto countStockCodeHistoryEntries() {
        CountResponse response = client.prepareCount(StockCodeHistoryEntryDto.INDEX_NAME)
                .execute()
                .actionGet();
        return new TotalCountDto(response.getCount());
    }

    @Override
    public List<StockCodeHistoryEntryDto> suggestStockCodeHistory(String term) {
        List<Suggest.Suggestion.Entry.Option> suggestions = suggest(term, StockCodeHistoryEntryDto.INDEX_NAME, "stock-code-history");
        return suggestions
                .stream()
                .map(option -> StockCodeHistoryEntryDto.fromOption(mapper, option))
                .collect(toList());
    }

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
