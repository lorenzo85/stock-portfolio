package org.stock.portfolio.providers.quandl.dto;

import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;

// CSV Entry example: WIKI/A,"Agilent Technologies, Inc. (A) Prices, Dividends, Splits and Trading Volume"
@CsvDataType()
public class StockCodeDto {

    @CsvField(pos = 0)
    private String datasetAndCode;

    @CsvField(pos = 1)
    private String description;

    public String getDataset() {
        return datasetAndCode.split("\\/")[0];
    }

    public String getCode() {
        return datasetAndCode.split("\\/")[1];
    }

    public String getDescription() {
        return description;
    }
}
