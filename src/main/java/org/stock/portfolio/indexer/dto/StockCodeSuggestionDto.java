package org.stock.portfolio.indexer.dto;

import org.elasticsearch.search.suggest.completion.CompletionSuggestion;

import static com.google.common.base.Preconditions.checkArgument;

public class StockCodeSuggestionDto {

    private final StockCodeDto suggestion;

    public StockCodeSuggestionDto(Object optionObject) {
        checkArgument(optionObject instanceof CompletionSuggestion.Entry.Option);

        CompletionSuggestion.Entry.Option option = (CompletionSuggestion.Entry.Option) optionObject;
        this.suggestion = StockCodeDto.deserializePayload(option.getPayloadAsString());
    }

    public StockCodeDto getSuggestion() {
        return suggestion;
    }
}
