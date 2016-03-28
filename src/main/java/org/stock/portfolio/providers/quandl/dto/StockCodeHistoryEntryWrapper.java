package org.stock.portfolio.providers.quandl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class StockCodeHistoryEntryWrapper {

    @JsonProperty("dataset_data")
    private StockCodeHistoryEntryDto stockCodeHistoryEntryDto;

    public StockCodeHistoryEntryDto getStockCodeHistoryEntryDto() {
        return stockCodeHistoryEntryDto;
    }
}
