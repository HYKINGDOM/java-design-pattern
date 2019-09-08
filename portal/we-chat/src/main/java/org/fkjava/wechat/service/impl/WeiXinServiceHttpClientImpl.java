package org.fkjava.wechat.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fkjava.commons.config.CommonConfigProperties;
import org.fkjava.commons.domain.Result;
import org.fkjava.wechat.domain.AccessToken;
import org.fkjava.wechat.domain.Tag;
import org.fkjava.wechat.domain.UserInfo;
import org.fkjava.wechat.message.send.OutMessage;
import org.fkjava.wechat.repository.jpa.TagRepository;
import org.fkjava.wechat.repository.jpa.UserInfoRepository;
import org.fkjava.wechat.repository.redis.AccessTokenRepository;
import org.fkjava.wechat.service.RedisLock;
import org.fkjava.wechat.service.WeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WeiXinServiceHttpClientImpl implements WeiXinService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private CommonConfigProperties commonConfigProperties;
    @Autowired
    private AccessTokenRepository accessTokenRepository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 当程序是多线程的，并且Map作为成员变量提供使用的时候，最好使用ConcurrentHashMap
    // ConcurrentHashMap支持多线程、并发。
    // 面试问题：HashMap和ConcurrentHashMap的区别是什么？
    // 两个其实都是线程不安全的，但是ConcurrentHashMap里面使用【乐观锁】的方式保证数据的安全。
    // 每当修改ConcurrentHashMap里面数据的时候，先检查是否被其他人修改过，如果修改过则重试。
//    private final Map<String, AccessToken> tokenMap = new ConcurrentHashMap<>();
    private CommonConfigProperties properties;

    public WeiXinServiceHttpClientImpl(CommonConfigProperties properties) {
        this.properties = properties;
    }

    @Override
    public AccessToken getAccessToken(String weixinAccount) {
//        AccessToken token = tokenMap.get(weixinAccount);
//        if (token != null && token.isNotExpired()) {
//            // 不为空，并且未过期，直接返回
//            return token;
//        }
//        // 此时由于只考虑单个节点（单台机），所以暂时不考虑分布式锁。
//        synchronized (tokenMap) {
//            // 调用远程接口获取令牌
//            token = getRemoteToken(weixinAccount);
//            tokenMap.put(weixinAccount, token);
//        }

        AccessToken token = this.accessTokenRepository.findById(weixinAccount).orElse(null);
        if (token != null && token.isNotExpired()) {
            return token;
        }

        // 增加分布式事务锁
        RedisLock lock = new RedisLock("ACCESS_TOKEN_LOCK_" + weixinAccount, stringRedisTemplate);
        lock.lock();

        // 加锁成功
        try {
            // 如果前面有其他程序锁定了微信账号，可能就是正在获取令牌
            // 所以当前程序得到锁以后，需要尝试再次获取缓存的令牌
            token = this.accessTokenRepository.findById(weixinAccount).orElse(null);
            if (token != null && token.isNotExpired()) {
                return token;
            }
            // 实在是没有令牌，才获取远程的令牌
            token = getRemoteToken(weixinAccount);
            // 把微信号作为令牌的ID来存储使用
            token.setId(weixinAccount);
            // 把令牌保存到Redis数据库
            this.accessTokenRepository.save(token);
        } finally {
            // 释放分布式事务锁
            lock.unlock();
        }

        log.trace("成功获取令牌，令牌的有效时间: {}，令牌的值: \n{}", new Date(token.getExpireTime()), token.getToken());
        return token;
    }

    private AccessToken getRemoteToken(String weixinAccount) {
        String uri = "/token";
        Map<String, String> params = new HashMap<>();

        CommonConfigProperties.WeChat account = properties.findWeChatByAccount(weixinAccount);
        params.put("appid", account.getAppId());
        params.put("secret", account.getAppSecret());
        params.put("grant_type", "client_credential");

        return HttpClientProxy.get(uri, AccessToken.class, params);
    }

    @Override
    public UserInfo getUserInfo(String weixinAccount, String openId) {
        AccessToken token = this.getAccessToken(weixinAccount);

        String uri = "/user/info";
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token.getToken());
        params.put("openid", openId);
        params.put("lang", "zh_CN");


        return HttpClientProxy.get(uri, UserInfo.class, params);
    }

    @Override
    public void send(OutMessage message) {
        AccessToken token = this.getAccessToken(message.getAccount());
        String uri = "/message/custom/send";
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token.getToken());
        HttpClientProxy.post(uri, params, message);
    }

    @Override
    public Page<Tag> findTags(String account, int pageNumber, String keyword) {
        Assert.notNull(account, "微信公众号的账号不能为空！");
        // 查询所有的非临时标签，如果有关键字则根据关键字搜索（名称）
        Sort sort = Sort.by(Sort.Order.asc("name"));//排序条件
        Pageable pageable = PageRequest.of(pageNumber, 10, sort);

        Specification<Tag> spec = (root, query, builder) -> {
            // 只查询temporary为true的 标签
            Predicate predicate = builder.isFalse(root.get("temporary"));
            predicate = builder.and(predicate, builder.equal(root.get("account"), account));

            if (!StringUtils.isEmpty(keyword)) {
                predicate = builder.and(predicate, builder.like(root.get("name"), "%" + keyword + "%"));
            }
            return predicate;
        };

        Page<Tag> page = this.tagRepository.findAll(spec, pageable);
        page.getContent().forEach(tag -> {
            // 在数据库保存的标签跟用户的关系
            List<UserInfo> userInfos = tag.getUserInfos();
            // 用于传递给页面上的标签跟用户的关闭
            List<String> users = tag.getUsers();
            userInfos.forEach(u -> {
                users.add(u.getId());
            });
        });

        return page;
    }

    @Override
    public Page<UserInfo> findUsers(String account, int pageNumber, String keyword) {
        // 查询所有的非临时标签，如果有关键字则根据关键字搜索（名称）
        Sort sort = Sort.by(Sort.Order.asc("nickName"));//排序条件
        Pageable pageable = PageRequest.of(pageNumber, 1000, sort);

        Specification<UserInfo> spec = (root, query, builder) -> {
            // 只查询temporary为true的 标签
            Predicate predicate = builder.isTrue(root.get("enable"));
            predicate = builder.and(predicate, builder.equal(root.get("account"), account));

            if (!StringUtils.isEmpty(keyword)) {
                predicate = builder.and(predicate, builder.like(root.get("nickName"), "%" + keyword + "%"));
            }
            return predicate;
        };

        Page<UserInfo> page = this.userInfoRepository.findAll(spec, pageable);
        List<UserInfo> users = page.getContent().stream()
                .map(user1 -> {
                    UserInfo user2 = new UserInfo();
                    user2.setId(user1.getId());
                    user2.setNickName(user1.getNickName());
                    return user2;
                })
                .collect(Collectors.toList());

        Page<UserInfo> newPage = new PageImpl<>(users, pageable, page.getTotalElements());
        return newPage;
    }

    @Override
    @Transactional
    public Result saveTag(Tag tag) {

        // 1.把标签对应的用户关联起来（把users里面的id转换为UserInfo对象放入userInfos集合里面）
        tag.setUserInfos(new LinkedList<>());
        tag.getUsers().forEach(userInfoId -> {
            UserInfo ui = this.userInfoRepository.findById(userInfoId).get();
            tag.getUserInfos().add(ui);
        });
        // 2.把标签保存到数据库
        tag.setTemporary(false);//自己添加的标签，就不是临时标签
        Tag savedTag = this.tagRepository.save(tag);
        // 3.把标签发送给微信公众号平台，保存以后会返回一个数字的id（Tag对象），需要更新本地数据库的Tag对象

        // 标签在页面上选择了某个公众号以后，本身就携带了公众号的微信账号，通过微信账号来查找配置参数。
        CommonConfigProperties.WeChat weChat = commonConfigProperties.getAllWeChats()
                .stream()
                .filter(wc -> wc.getAccount().equals(tag.getAccount()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("公众号配置错误：根据微信账号 [" + tag.getAccount() + "] 未找到公众号"));

        AccessToken token = this.getAccessToken(weChat.getAccount());
        String uri = "/tags/create";
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token.getToken());

        // 提交给公众号的数据不确定，所以直接发送Map对象
        Map<String, Map<String, Object>> data = new HashMap();

        Map<String, Object> tagMap = new HashMap<>();
        tagMap.put("name", tag.getName());
        data.put("tag", tagMap);

        Map<String, ?> result = HttpClientProxy.post(uri, params, data);

        Map<?, ?> resultTagMap = (Map<?, ?>) result.get("tag");
        if (resultTagMap == null) {
            // 出现错误的情况下就没有名为tag的属性返回
            throw new RuntimeException("保存标签出现问题: " + result.toString());
        }
        // 把id更新到本地数据库
        Integer id = (Integer) resultTagMap.get("id");
        tag.setId(id);
//        this.tagRepository.save(tag);

        // 把用户批量添加到标签里面
        List<String> openIds = new LinkedList<>();
        tag.getUserInfos().forEach(userInfo -> {
            openIds.add(userInfo.getOpenId());
        });
        Map<String, Object> addUsersMap = new HashMap<>();
        addUsersMap.put("openid_list", openIds);
        addUsersMap.put("tagid", id);
        HttpClientProxy.post("/tags/members/batchtagging", params, addUsersMap);

        return Result.ok("标签保存成功");
    }
}
