package org.fkjava.commons.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToStringConverter {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public String convert(Date date) {
        return DATE_FORMAT.format(date);
    }
}
