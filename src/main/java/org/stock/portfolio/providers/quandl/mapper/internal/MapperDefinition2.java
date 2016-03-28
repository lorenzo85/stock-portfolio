package org.stock.portfolio.providers.quandl.mapper.internal;

import org.joda.time.DateTime;
import org.stock.portfolio.stockcodehistory.StockCodeHistory;

import java.util.Arrays;
import java.util.List;

public class MapperDefinition2 extends AbstractMapperDefinition {

        private List<String> SUPPORTED_COLUMNS = Arrays.asList(
                "Date",
                "Open",
                "High",
                "Low",
                "Close",
                "Volume",
                "Ex-Dividend",
                "Split Ratio",
                "Adj. Open",
                "Adj. High",
                "Adj. Low",
                "Adj. Close",
                "Adj. Volume");

        @Override
        public StockCodeHistory map(List<Object> object) {
            StockCodeHistory entry = new StockCodeHistory();
            DateTime date = parseDate(object.get(0));
            entry.setDate(date);
            entry.setYear(parseYear(date));
            entry.setOpenPrice(parseDouble(object.get(1)));
            entry.setHighPrice(parseDouble(object.get(2)));
            entry.setLowPrice(parseDouble(object.get(3)));
            entry.setClosePrice(parseDouble(object.get(4)));
            return entry;
        }

        @Override
        public List<String> getSupportedColumnsNames() {
            return SUPPORTED_COLUMNS;
        }


}
