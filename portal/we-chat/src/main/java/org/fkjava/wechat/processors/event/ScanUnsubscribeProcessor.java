package org.fkjava.wechat.processors.event;

import org.fkjava.wechat.message.EventMessage;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 未关注时扫描带参数的二维码
 */
@Component
public class ScanUnsubscribeProcessor {

    @EventListener(
            value = EventMessage.class,
            // T(org.springframework.util.StringUtils)得到类本身，用于调用静态的成员
            condition = "#message.event == 'subscribe'" +
                    " and !T(org.springframework.util.StringUtils).isEmpty(#message.eventKey)"
    )
    @Async
    public void onSubscribe(EventMessage message) {
        System.out.println("扫描带参数二维码关注");
    }
}
