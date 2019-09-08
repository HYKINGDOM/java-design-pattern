package org.fkjava.commons.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeConverter4Beijing extends StdConverter<Date, String> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

    @Override
    public String convert(Date value) {
        return DATE_FORMAT.format(value);
    }
}
