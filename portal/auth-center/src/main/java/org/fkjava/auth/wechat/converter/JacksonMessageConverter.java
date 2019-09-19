package org.fkjava.auth.wechat.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Arrays;

public class JacksonMessageConverter extends MappingJackson2HttpMessageConverter {

    public JacksonMessageConverter() {
        super.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, new MediaType("application", "*+json"), MediaType.TEXT_PLAIN));
    }
}
