package org.fkjava.commons.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.Date;

public class StringToDateTimeConverter extends StdConverter<String, Date> {

    private static final StringToDateTimePropertyEditor propertyEditor = new StringToDateTimePropertyEditor();

    @Override
    public Date convert(String value) {
        return propertyEditor.convert(value);
    }
}
