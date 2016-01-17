package org.stock.portfolio.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultDateFormat extends SimpleDateFormat {

    public DefaultDateFormat(String pattern) {
        super(pattern);
    }

    public Date parseString(String source) {
        try {
            return parse(source);
        } catch (ParseException e) {
            throw new RuntimeException(String.format("Could not parse %s", source));
        }
    }

}
