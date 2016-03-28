package org.stock.portfolio.providers.quandl.commons;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;


public class DefaultDateFormat {

    private final DateTimeFormatter pattern;

    public DefaultDateFormat(String pattern) {
        this.pattern = DateTimeFormat.forPattern(pattern);
    }

    public DateTime parseString(String source) {
        return pattern.parseDateTime(source);
    }

    public String format(Date date) {
        return Integer.toString(new DateTime(date).getYear());
    }
}
