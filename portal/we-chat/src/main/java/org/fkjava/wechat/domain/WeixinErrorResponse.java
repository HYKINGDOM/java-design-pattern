package org.fkjava.wechat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 封装微信返回的错误信息
 */
@Getter
@Setter
public class WeixinErrorResponse extends RuntimeException {

    @JsonProperty("errcode")
    private String errorCode;
    @JsonProperty("errmsg")
    private String errorMessage;

    @Override
    public String getMessage() {
        return "错误代码: " + errorCode + "，错误信息: " + errorMessage;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
