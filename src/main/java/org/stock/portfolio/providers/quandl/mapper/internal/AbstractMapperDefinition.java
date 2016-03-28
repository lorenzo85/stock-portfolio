package org.stock.portfolio.providers.quandl.mapper.internal;

import org.joda.time.DateTime;
import org.stock.portfolio.providers.quandl.commons.DefaultDateFormat;
import org.stock.portfolio.providers.quandl.mapper.Mapper;
import org.stock.portfolio.stockcodehistory.StockCodeHistory;

import java.util.Date;
import java.util.List;

import static java.lang.String.format;

public abstract class AbstractMapperDefinition implements ColumnsDefinition, Mapper<List<Object>, StockCodeHistory> {

    private static final DefaultDateFormat DATE_FORMAT = new DefaultDateFormat("yyyy-MM-dd");

        protected DateTime parseDate(Object o) {
            if (o instanceof Date) {
                return new DateTime(o);
            } else if (o instanceof String) {
                return DATE_FORMAT.parseString((String) o);
            } else {
                throw new RuntimeException(format("Could not parse date for object %s", o));
            }
        }

        protected double parseDouble(Object o) {
            if (o == null) return 0;
            return (Double) o;
        }

        protected String parseYear(DateTime date) {
            return Integer.toString(date.getYear());
        }

}
