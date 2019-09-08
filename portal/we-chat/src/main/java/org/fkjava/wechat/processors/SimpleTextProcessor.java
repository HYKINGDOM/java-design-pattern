package org.fkjava.wechat.processors;

import org.fkjava.wechat.message.TextMessage;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SimpleTextProcessor {

    @EventListener(TextMessage.class)
    @Async// 异步处理
    public void onMessage(TextMessage message) {
        System.out.println("处理简单的文本消息");
    }

    @EventListener(TextMessage.class)
    @Async
    public void onMessage2(TextMessage message) {
        System.out.println("处理简单的文本消息，另外一个处理程序");
    }
}
