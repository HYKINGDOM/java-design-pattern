package org.fkjava.wechat.processors.event;

import org.fkjava.wechat.message.EventMessage;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 已关注时扫描带参数的二维码
 */
@Component
public class ScanSubscribeProcessor {

    @EventListener(
            value = EventMessage.class,
            condition = "#message.event == 'SCAN'"
    )
    @Async
    public void onSubscribe(EventMessage message) {
        System.out.println("扫描带参数二维码");
    }
}
