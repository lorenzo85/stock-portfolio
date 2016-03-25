package org.stock.portfolio.service.quandl.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockCodeHistoryEntryDto {

    @JsonProperty("column_names")
    private List<String> columnNames;
    @JsonProperty("start_date")
    private Date startDate;
    @JsonProperty("end_date")
    private Date endDate;
    @JsonProperty("frequency")
    private String frequency;
    @JsonProperty("data")
    private List<List<Object>> codeHistoryEntries;

    public List<List<Object>> getCodeHistoryEntries() {
        return codeHistoryEntries;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }
}
