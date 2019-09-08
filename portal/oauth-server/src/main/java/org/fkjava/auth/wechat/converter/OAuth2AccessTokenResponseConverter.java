package org.fkjava.auth.wechat.converter;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;

import java.util.Arrays;
import java.util.Map;

public class OAuth2AccessTokenResponseConverter extends OAuth2AccessTokenResponseHttpMessageConverter {

    private final ParameterizedTypeReference<Map<String, String>> PARAMETERIZED_RESPONSE_TYPE = new ParameterizedTypeReference<>() {
    };

    public OAuth2AccessTokenResponseConverter() {
        super.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, new MediaType("application", "*+json"), MediaType.TEXT_PLAIN));
    }

    @Override
    protected OAuth2AccessTokenResponse readInternal(Class<? extends OAuth2AccessTokenResponse> clazz, HttpInputMessage inputMessage)
            throws HttpMessageNotReadableException {
        try {

            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
            @SuppressWarnings("unchecked")
            Map<String, String> tokenResponseParameters = (Map<String, String>) mappingJackson2HttpMessageConverter.read(
                    PARAMETERIZED_RESPONSE_TYPE.getType(), null, inputMessage);

            // 需要手动把令牌类型加上，否则Spring Security会验证失败
            tokenResponseParameters.put(OAuth2ParameterNames.TOKEN_TYPE, "Bearer");
            return super.tokenResponseConverter.convert(tokenResponseParameters);
        } catch (Exception e) {
            throw new HttpMessageNotReadableException(e.getLocalizedMessage(), e, inputMessage);
        }
    }
}
