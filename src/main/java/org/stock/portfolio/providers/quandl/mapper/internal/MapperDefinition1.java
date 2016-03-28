package org.stock.portfolio.providers.quandl.mapper.internal;

import org.joda.time.DateTime;
import org.stock.portfolio.stockcodehistory.StockCodeHistory;

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
    public StockCodeHistory map(List<Object> object) {
        StockCodeHistory entry = new StockCodeHistory();
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
