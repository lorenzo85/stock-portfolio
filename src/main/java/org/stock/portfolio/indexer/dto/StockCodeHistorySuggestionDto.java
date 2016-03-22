package org.stock.portfolio.indexer.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;

import static com.google.common.base.Preconditions.checkArgument;

public class StockCodeHistorySuggestionDto {

    private StockCodeHistoryEntryDto suggestion;

    public static StockCodeHistorySuggestionDto fromOption(ObjectMapper mapper, Object option) {
        checkArgument(option instanceof CompletionSuggestion.Entry.Option);

        CompletionSuggestion.Entry.Option completion = (CompletionSuggestion.Entry.Option) option;

        StockCodeHistorySuggestionDto dto = new StockCodeHistorySuggestionDto();
        dto.suggestion = StockCodeHistoryEntryDto.deserializePayload(mapper, completion.getPayloadAsString());
        return dto;
    }

    public StockCodeHistoryEntryDto getSuggestion() {
        return suggestion;
    }
}
