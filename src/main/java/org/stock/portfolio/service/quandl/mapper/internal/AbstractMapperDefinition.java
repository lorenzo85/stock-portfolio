package org.stock.portfolio.service.quandl.mapper.internal;

import org.joda.time.DateTime;
import org.stock.portfolio.domain.StockHistoryEntry;
import org.stock.portfolio.service.commons.DefaultDateFormat;
import org.stock.portfolio.service.quandl.mapper.Mapper;

import java.util.Date;
import java.util.List;

import static java.lang.String.format;

public abstract class AbstractMapperDefinition implements ColumnsDefinition, Mapper<List<Object>, StockHistoryEntry> {

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
