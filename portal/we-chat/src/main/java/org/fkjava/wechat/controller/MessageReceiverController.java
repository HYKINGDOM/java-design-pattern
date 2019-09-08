package org.fkjava.wechat.controller;

import org.fkjava.wechat.message.ReceiveMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// 要求返回XML，收到的也是XML
@Controller
@RequestMapping("/receiver")
public class MessageReceiverController implements ApplicationEventPublisherAware {

    private Logger log = LoggerFactory.getLogger(MessageReceiverController.class);
    private ApplicationEventPublisher applicationEventPublisher;

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationEventPublisher(
            ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // 在公众号平台上面配置URL的时候使用的，用于判断我们的服务器是否能够正常接收信息。
    @GetMapping
    @ResponseBody
    public String echo(
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("echostr") String echostr
    ) {
        log.debug("接入认证消息: \n  签名 {}\n  时间戳 {}\n  随机数 {}",
                signature, timestamp, nonce);
        // 验证成功以后，把echostr原文返回
        return echostr;
    }

    @PostMapping
    @ResponseBody
    public String onMessage(
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestBody String xml
    ) {
        log.debug("微信客户端消息: \n  签名 {}\n  时间戳 {}\n  随机数 {}  消息内容 {}",
                signature, timestamp, nonce, xml);
        // 需要根据不同的MsgType转换为不同的消息类型。
        // 也可以把收到的原文，直接放入消息队列。
        // 通过消息的处理器来转换消息，转换后的消息再放入另外一个队列。
        applicationEventPublisher.publishEvent(new ReceiveMessageEvent(xml));

        // 返回success的时候，表示消息已经接收，并且正在处理。如果没有正确返回微信会重发消息。
        // 一般的做法是：收到消息，直接返回成功，然后在后台使用异步的方式处理消息。
        // 处理完成以后，通过客服接口给用户返回结果。
        // 有时候也会在收到消息以后，马上下发一条广告。
        return "success";
    }
}
