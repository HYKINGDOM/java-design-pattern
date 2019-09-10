package org.fkjava.wechat.service;

import org.fkjava.commons.domain.Result;
import org.fkjava.wechat.domain.AccessToken;
import org.fkjava.wechat.domain.Tag;
import org.fkjava.wechat.domain.UserInfo;
import org.fkjava.wechat.message.send.OutMessage;
import org.springframework.data.domain.Page;

public interface WeiXinService {

    /**
     * 每次调用远程接口的时候，都要携带访问令牌。每隔2小时要更新一次令牌。<br>
     * 得到令牌以后，需要把令牌缓存起来，然后在过期的时候自动更新。
     *
     * @param weixinAccount 获取访问令牌的时候，需要根据微信号找到appid和appsecret参数。
     * @return 未过期的令牌
     */
    AccessToken getAccessToken(String weixinAccount);

    /**
     * 此方法的参数不需要传入访问令牌，而是在实现里面自己调用{@link #getAccessToken(String)}方法来得到令牌。<br>
     * 返回的信息里面包含了一个unionid，此在公众号加入了某个开放平台账号以后才会有。<br>
     * 微信公众号属于微信开放平台的一部分。开放平台可以绑定多个公众号、小程序，这些绑定的账号返回的unionid是相同的。
     *
     * @param weixinAccount 获取哪个公众号里面的用户的信息
     * @param openId        用户在公众号的ID。
     * @return 微信返回的用户信息。
     */
    UserInfo getUserInfo(String weixinAccount, String openId);

    void send(OutMessage message);

    Page<Tag> findTags(String account, int pageNumber, String keyword);

    Page<UserInfo> findUsers(String account, int pageNumber, int pageSize, String keyword);

    Result saveTag(Tag tag);

    Page<UserInfo> findUserNames(String account, int pageNumber, int pageSize, String keyword);

    UserInfo findUserById(String openId);

    Result updateRemark(String id, String remark);
}
