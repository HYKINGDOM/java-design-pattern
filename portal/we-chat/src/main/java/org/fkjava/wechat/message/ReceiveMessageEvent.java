package org.fkjava.wechat.message;

import org.springframework.context.ApplicationEvent;

public class ReceiveMessageEvent extends ApplicationEvent {

    private String xml;

    /**
     * @param xml 微信公众号发送给我们的XML内容
     */
    public ReceiveMessageEvent(String xml) {
        super(xml);
        this.xml = xml;
    }


    public String getXml() {
        return xml;
    }
}
