package org.fkjava.wechat.message;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageMessage extends Message {

    @JsonProperty("PicUrl")
    private String url;
    @JsonProperty("MediaId")
    private String mediaId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
