package org.fkjava.commons.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "fk")
@Getter
@Setter
public class CommonConfigProperties {
    /**
     * 默认值 http://localhost:20001
     */
    private String hostPrefix = "http://localhost:20001";
    /**
     * 默认值是一个空字符串
     */
    private String apiPath = "";
    /**
     * 默认值是 hostPrefix + apiPath
     */
    private String apiPrefix = hostPrefix + apiPath;
    private WeChatLogin weChatLogin;
    /**
     * 以公众号的微信号为key存储
     */
    private Map<String, WeChat> weChats = new HashMap<>();
    private Function<Map.Entry<String, WeChat>, WeChat> weChatFunction = kv -> {
        kv.getValue().setAccount(kv.getKey());
        return kv.getValue();
    };

    public Collection<WeChat> getAllWeChats() {
        return weChats.entrySet().stream().map(weChatFunction).collect(Collectors.toList());
    }

    public WeChat findWeChatByAccount(String account) {
        return weChats.entrySet().stream()
                .filter(kv -> kv.getKey().equals(account))
                .findFirst()
                .map(weChatFunction)
                .orElseThrow(() -> new RuntimeException("未找到公众号微信号 [" + account + "] 对应的配置"));
    }

    public WeChat findWeChatByAppId(String appId) {

        return weChats.entrySet().stream()
                .filter(kv -> kv.getValue().getAppId().equals(appId))
                .findFirst()
                .map(weChatFunction)
                .orElseThrow(() -> new RuntimeException("未找到公众号AppId [" + appId + "] 对应的配置"));
    }

    @Getter
    @Setter
    public static class WeChatLogin {
        /**
         * 登录服务的路径
         */
        private String servicePath = "/oauth-server";
        /**
         * 重定向时的URL模板，用于生成重定向地址
         */
        private String redirectUri = "/oauth-server/wechat/{action}/oauth2/code/{registrationId}";
        /**
         * 处理微信登录的URL片段，完整URL会加上{@link CommonConfigProperties#apiPrefix}的值。
         */
        private String loginProcessingUrl = "/wechat/login/oauth2/code/*";
    }

    @Getter
    @Setter
    public static class WeChat {

        private String name;
        /**
         * 公众号的微信号，以外部的Map的key为准
         */
        private String account;
        /**
         * 公众号的唯一ID
         */
        private String appId;
        /**
         * 公众号程序的密钥，在公众号平台上面随机生成且不会保存，需要自己保存下来。
         */
        private String appSecret;
        /**
         * 验证消息的合法性时使用，比如微信公众号发送一个消息给我们的服务器，需要才要此字段的值参与计算合法性。
         */
        private String token;
        /**
         * 加密通讯时使用的密钥，用于加密数据和解密数据。
         */
        private String encodingKey;
    }
}
