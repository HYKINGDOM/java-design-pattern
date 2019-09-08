package org.fkjava.wechat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@Setter
// 使用@RedisHash注解，表示对象在Redis里面存储以Hash的方式存储
// 在Redis里面，Hash其实就是键值对
// 指定的名称是Redis里面Hash的名称，也是对象的前缀
// Spring Data Redis为了方便管理对象，所有每个对象的Hash都存储在一个Set里面，Set的名称为WeChatAccessToken
@RedisHash("WeChatAccessToken")
public class AccessToken implements Serializable {

    // 这个id不能自动生成，需要手动设置，这里直接使用微信公众号的微信号作为主键
    @Id // 使用Spring的@Id注解，表示它是一个主键，但是此主键跟SQL数据库没有关系
    private String id;
    @JsonProperty("access_token")
    private String token;
    @JsonProperty("expires_in")
    private long expiresIn;

    // 创建本地的令牌时的时间
    private long time = System.currentTimeMillis();
    private long expireTime;

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        // 减去120表示提前120秒过期
        this.expireTime = time + ((expiresIn - 120) * 1000);
    }

    public boolean isNotExpired() {
        // 判断是否过期的瞬间
        long current = System.currentTimeMillis();
        // 当前时间，小于令牌创建时间+过期时间
        // 比如当前时间是12点，令牌是9点创建的，令牌的过期时间是2小时
        return current <= expireTime;
    }
}
