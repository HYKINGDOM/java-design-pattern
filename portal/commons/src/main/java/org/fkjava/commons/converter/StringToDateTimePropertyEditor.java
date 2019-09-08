package org.fkjava.commons.converter;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

// 通用的字符串转换为Date的转换器，支持多种格式转换。增加格式的时候注意：更精确的格式放前面。
public class StringToDateTimePropertyEditor extends PropertyEditorSupport {

    // "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSS", "EEE, dd MMM yyyy HH:mm:ss zzz", "yyyy-MM-dd"
    private static final String[] FORMATS = {"yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "EEE, dd MMM yyyy HH:mm:ss zzz",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy年MM月dd日HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy年MM月dd日"
    };
    private static final SimpleDateFormat[] DATE_FORMATS;

    static {
        DATE_FORMATS = new SimpleDateFormat[FORMATS.length];
        for (int i = 0; i < FORMATS.length; i++) {
            DATE_FORMATS[i] = new SimpleDateFormat(FORMATS[i]);
        }
    }

    Date convert(String value) {
        try {
            for (SimpleDateFormat DATE_FORMAT : DATE_FORMATS) {
                return DATE_FORMAT.parse(value);
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("字符串 " + value + " 不能转换为Date对象，允许的时间格式为: " + Arrays.toString(FORMATS), e);
        }
        throw new IllegalArgumentException("字符串 " + value + " 不能转换为Date对象，未找到合适的转换格式，允许的时间格式为: " + Arrays.toString(FORMATS));
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Date date = this.convert(text);
        super.setValue(date);
    }
}
