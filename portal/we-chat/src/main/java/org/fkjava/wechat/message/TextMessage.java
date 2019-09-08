package org.fkjava.wechat.message;


import com.fasterxml.jackson.annotation.JsonProperty;

public class TextMessage extends Message {

    @JsonProperty("Content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
