package org.stock.portfolio.indexer.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;

import static com.google.common.base.Preconditions.checkArgument;

public class StockCodeSuggestionDto {

    private StockCodeDto suggestion;

    public static StockCodeSuggestionDto fromOption(ObjectMapper mapper, Object option) {
        checkArgument(option instanceof CompletionSuggestion.Entry.Option);

        CompletionSuggestion.Entry.Option completion = (CompletionSuggestion.Entry.Option) option;

        StockCodeSuggestionDto dto = new StockCodeSuggestionDto();
        dto.suggestion = StockCodeDto.deserializePayload(mapper, completion.getPayloadAsString());
        return dto;
    }

    public StockCodeDto getSuggestion() {
        return suggestion;
    }
}
