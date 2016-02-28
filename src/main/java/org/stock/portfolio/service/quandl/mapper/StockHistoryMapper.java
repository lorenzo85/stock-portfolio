package org.stock.portfolio.service.quandl.mapper;

import org.springframework.stereotype.Component;
import org.stock.portfolio.service.commons.DefaultDateFormat;
import org.stock.portfolio.domain.StockHistoryEntry;
import org.stock.portfolio.service.quandl.dto.StockHistoryDto;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
public class StockHistoryMapper implements Mapper<StockHistoryDto, Collection<StockHistoryEntry>> {

    private static final DefaultDateFormat DATE_FORMAT = new DefaultDateFormat("yyyy-MM-dd");
    private static final DefaultDateFormat DATE_YEAR = new DefaultDateFormat("yyyy");
    private static final List<ColumnsDefinition> MAPPERS = Arrays.asList(
            new TypeColumnDef1(),
            new TypeColumnDef2());


    @Override
    public Collection<StockHistoryEntry> map(StockHistoryDto stockHistory) {
        ColumnsDefinition mapper = getMapper(stockHistory.getColumnNames());

        return stockHistory.getCodeHistoryEntries()
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    private ColumnsDefinition getMapper(List<String> columnsNames) {
        for (ColumnsDefinition definition : MAPPERS) {
            List<String> supportedColumnsNames = definition.getSupportedColumnsNames();
            if (supportedColumnsNames.equals(columnsNames)) return definition;
        }
        throw new IllegalArgumentException(format("No available mapper for columns names %s", columnsNames));
    }


    interface ColumnsDefinition extends Mapper<List<Object>, StockHistoryEntry> {
        List<String> getSupportedColumnsNames();
    }

    static abstract class AbstractColumnDef implements ColumnsDefinition {

        protected Date parseDate(Object o) {
            if (o instanceof Date) {
                return (Date) o;
            } else if (o instanceof String) {
                return DATE_FORMAT.parseString((String) o);
            } else {
                throw new RuntimeException(format("Could not parse date for object %s", o));
            }
        }

        protected double parseDouble(Object o) {
            return (Double) o;
        }

        protected String parseYear(Date date) {
            return DATE_YEAR.format(date);
        }
    }


    static class TypeColumnDef1 extends AbstractColumnDef {

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
        public StockHistoryEntry map(List<Object> object) {
            StockHistoryEntry entry = new StockHistoryEntry();
            Date date = parseDate(object.get(0));
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


    static class TypeColumnDef2 extends AbstractColumnDef {

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
            Date date = parseDate(object.get(0));
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

}
