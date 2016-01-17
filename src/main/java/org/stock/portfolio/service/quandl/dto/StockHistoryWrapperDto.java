package org.stock.portfolio.service.quandl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class StockHistoryWrapperDto {

    @JsonProperty("dataset_data")
    private StockHistoryDto stockHistoryDto;

    public StockHistoryDto getStockHistoryDto() {
        return stockHistoryDto;
    }
}
