package org.fkjava.wechat.message;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString(callSuper = true)
public abstract class Message {
    /**
     * 开发者微信号
     */
    @JsonProperty("ToUserName")
    private String toUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    @JsonProperty("FromUserName")
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    @JsonProperty("CreateTime")
    private String createTime;
    /**
     * 消息类型，地理位置为location
     */
    @JsonProperty("MsgType")
    private String msgType;
    /**
     * 消息id，64位整型
     */
    @JsonProperty("MsgId")
    private String msgId;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
