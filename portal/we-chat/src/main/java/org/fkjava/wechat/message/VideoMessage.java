package org.fkjava.wechat.message;


import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoMessage extends Message {
    @JsonProperty("MediaId")
    private String mediaId;
    @JsonProperty("ThumbMediaId")
    private String thumbMediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}
