package org.fkjava.wechat.processors.event;


import org.fkjava.commons.config.CommonConfigProperties;
import org.fkjava.user.api.RemoteUserService;
import org.fkjava.user.domain.User;
import org.fkjava.wechat.domain.UserInfo;
import org.fkjava.wechat.message.EventMessage;
import org.fkjava.wechat.message.send.NewsMessage;
import org.fkjava.wechat.repository.jpa.UserInfoRepository;
import org.fkjava.wechat.service.WeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * 普通关注
 */
@Component
public class SubscribeProcessor implements ApplicationEventPublisherAware {

    private final WeiXinService weiXinService;
    private final RemoteUserService userCenter;
    private final UserInfoRepository userInfoRepository;
    private final CommonConfigProperties commonConfigProperties;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private ApplicationEventPublisher applicationEventPublisher;

    public SubscribeProcessor(
            WeiXinService weiXinService,
            RemoteUserService userCenter,
            UserInfoRepository userInfoRepository,
            CommonConfigProperties commonConfigProperties
    ) {
        this.weiXinService = weiXinService;
        this.userCenter = userCenter;
        this.userInfoRepository = userInfoRepository;
        this.commonConfigProperties = commonConfigProperties;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationEventPublisher(
            ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @EventListener(
            value = EventMessage.class,
            condition = "#message.event == 'subscribe' " +
                    "and T(org.springframework.util.StringUtils).isEmpty(#message.eventKey)"
    )
    @Async
    public void onSubscribe(EventMessage message) {
        // 调用远程接口获取用户信息
        UserInfo userInfo = this.weiXinService.getUserInfo(message.getToUserName(), message.getFromUserName());
        // 把用户信息跟我们自己的账户系统关联起来
        User user = this.userCenter.byOpenId(message.getFromUserName());
        String userId;
        if (user == null) {
            // 表示之前没有注册！
            User newUser = new User();
            newUser.setOpenId(message.getFromUserName());
            // 后续可以提供一个页面，用于给用户修改登录名，因为openId作为登录名根本无法输入（记不住）
            // 一般建议登录名只能修改一次！
            newUser.setLoginName(message.getFromUserName());
            newUser.setName(userInfo.getNickName());//把微信的昵称作为用户姓名来使用
            userId = this.userCenter.registry(newUser).getId();
        } else {
            userId = user.getId();
        }
        userInfo.setUserId(userId);
        userInfo.setAccount(message.getToUserName());

        // 把UserInfo保存到微信服务的模块里面
        // 需要检查OpenId是否在本地数据库有对应的记录，如果有则修改数据；否则才新增。
        UserInfo old = this.userInfoRepository.findByOpenId(userInfo.getOpenId());
        if(old != null){
            userInfo.setId(old.getId());
        }
        userInfo.setEnable(true);
        this.userInfoRepository.save(userInfo);

        // 返回一个链接消息给客户端，让客户端点击以后可以设置手机号码、登录名、密码。这个功能由user-center来提供，并且通过网关来访问。
        // 在使用此功能的时候，还需要登录、授权。
        // 此时需要注意：如果没有密码，只能通过微信网页授权的方式来登录。
        // 原来是通过登录名、密码来完成登录操作的，现在要增加、扩展能够通过微信登录来授权。
        // 1.返回链接信息给微信客户端，此处只需要考虑第一步
        NewsMessage msg = new NewsMessage();
        msg.setToUser(message.getFromUserName());
        msg.setAccount(message.getToUserName());
        NewsMessage.Article article = new NewsMessage.Article();
        article.setTitle("完成注册");
        article.setDescription("请点击链接以后，输入手机号码和密码，方便后面的使用！");
        article.setPicUrl(commonConfigProperties.getHostPrefix() + "/we-chat/logo.png");
        article.setUrl(commonConfigProperties.getApiPrefix() + "/we-chat/user/registry/complete");
        msg.setArticle(article);

        // 把消息放入队列中，另外再来发送消息
        applicationEventPublisher.publishEvent(msg);

        // 把消息发送到Redis队列中
        redisTemplate.convertAndSend("user-registered", message);


        // 2.微信网页授权，通过微信来登录
    }
}
