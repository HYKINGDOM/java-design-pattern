package org.fkjava.wechat.message;


import com.fasterxml.jackson.annotation.JsonProperty;

public class LinkMessage extends Message {
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Url")
    private String url;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
