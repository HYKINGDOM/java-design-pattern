package org.fkjava.wechat.processors;

import org.fkjava.wechat.message.send.OutMessage;
import org.fkjava.wechat.service.WeiXinService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SendMessageProcessor {

    private final WeiXinService weiXinService;

    public SendMessageProcessor(WeiXinService weiXinService) {
        this.weiXinService = weiXinService;
    }

    @EventListener(
            value = OutMessage.class
    )
    @Async
    public void onMessage(OutMessage message) {
        weiXinService.send(message);
    }
}
