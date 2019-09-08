package org.fkjava.wechat.processors;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.fkjava.wechat.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class XmlToObjectMessageProcessor
        implements ApplicationListener<ReceiveMessageEvent>,
        ApplicationEventPublisherAware {

    private XmlMapper xmlMapper = new XmlMapper();

    private Logger log = LoggerFactory.getLogger(XmlToObjectMessageProcessor.class);
    private Map<String, Class<? extends Message>> typeMap = new HashMap<>();
    private ApplicationEventPublisher applicationEventPublisher;

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationEventPublisher(
            ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public XmlToObjectMessageProcessor() {
        typeMap.put("text", TextMessage.class);
        typeMap.put("image", ImageMessage.class);
        typeMap.put("link", LinkMessage.class);
        typeMap.put("location", LocationMessage.class);
        typeMap.put("shortvideo", ShortVideoMessage.class);
        typeMap.put("video", VideoMessage.class);
        typeMap.put("voice", VoiceMessage.class);
        typeMap.put("event", EventMessage.class);
    }

    public void onApplicationEvent(ReceiveMessageEvent event) {
        // 必须要根据MsgType，找到合适的对象，然后使用Jackson把XML对象
        // 1.把所有的MsgType跟Java类使用一个Map关联起来
        // 2.把xml里面的MsgType截取出来，到Map里面获取Java类型
        String xml = event.getXml();
        String msgType = xml.substring(xml.indexOf("<MsgType><![CDATA[") + 18);
        msgType = msgType.substring(0, msgType.indexOf("]]></MsgType>"));

        Class<? extends Message> messageClass = typeMap.get(msgType);
        // 3.调用XmlMapper把XML转换为对象类型的对象
        try {
            Message message = xmlMapper.readValue(xml, messageClass);

            applicationEventPublisher.publishEvent(message);
        } catch (IOException e) {
            log.error("消息转换出现问题：" + e.getLocalizedMessage(), e);
        }
    }
}
