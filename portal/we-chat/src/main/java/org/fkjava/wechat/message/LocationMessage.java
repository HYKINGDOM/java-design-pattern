package org.fkjava.wechat.message;


import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationMessage extends Message {
    /**
     * 地理位置维度
     */
    @JsonProperty("Location_X")
    private String locationX;
    /**
     * 地理位置经度
     */
    @JsonProperty("Location_Y")
    private String locationY;
    /**
     * 地图缩放大小
     */
    @JsonProperty("Scale")
    private String scale;
    /**
     * 地理位置信息
     */
    @JsonProperty("Label")
    private String label;

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
