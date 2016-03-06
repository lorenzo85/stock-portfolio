package org.stock.portfolio.service.quandl.mapper.internal;

import org.joda.time.DateTime;
import org.stock.portfolio.domain.StockHistoryEntry;

import java.util.Arrays;
import java.util.List;

public class MapperDefinition1 extends AbstractMapperDefinition {

    private List<String> SUPPORTED_COLUMNS = Arrays.asList(
            "Date",
            "Price",
            "High",
            "Low",
            "Volume",
            "Last Close",
            "Change",
            "Var%");

    @Override
    public StockHistoryEntry map(List<Object> object) {
        StockHistoryEntry entry = new StockHistoryEntry();
        DateTime date = parseDate(object.get(0));
        entry.setDate(date);
        entry.setYear(parseYear(date));
        entry.setOpenPrice(parseDouble(object.get(1)));
        entry.setHighPrice(parseDouble(object.get(2)));
        entry.setLowPrice(parseDouble(object.get(3)));
        entry.setClosePrice(parseDouble(object.get(5)));
        return entry;
    }

    @Override
    public List<String> getSupportedColumnsNames() {
        return SUPPORTED_COLUMNS;
    }

}
