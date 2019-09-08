package org.fkjava.auth.config;

import org.fkjava.commons.config.CommonConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration
// 激活授权服务器的能力
@EnableAuthorizationServer
public class AuthenticationServerConfig
        extends AuthorizationServerConfigurerAdapter {

    // 如果想要正常获得AuthenticationManager，就需要在LoginConfig中暴露该Bean到容器
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CommonConfigProperties commonConfigProperties;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        String prefix = commonConfigProperties.getHostPrefix();
        clients.inMemory()// 内存模式
                // 在访问/oauth/authorize和/oauth/token都要加上的client_id参数
                .withClient("gateway")//指定一个客户端
//                .resourceIds("abc", "def")
                // 访问/oauth/token的时候使用client_secret参数
                .secret(passwordEncoder.encode("gateway_password"))//必须跟客户端要保持一致
                .scopes("all")
                // grant_type参数的值，在访问/oauth/authorize的时候只能使用authorization_code作为值
                .authorizedGrantTypes(
                        "authorization_code",
                        "password",
                        "token",
                        "implicit"
                )
                // 为了安全起见，一定把客户端的重定向地址记录在这里，否则可能会被拦截造成token的泄露
                .redirectUris(prefix + "/login/oauth2/code/gateway")
                // 自动确认，不需要用户点击按钮！
                .autoApprove(true)
        ;
    }

    // 设置允许表单授权访问客户端
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
        // 设置密码的加密程序
        security.passwordEncoder(passwordEncoder);
        security
                // 检查令牌是否有效的地址的权限
                // /oauth/check_token
                .checkTokenAccess("permitAll()")
                // 获取JWT令牌公钥的地址的访问权限
                // /oauth/token_key
                .tokenKeyAccess("permitAll()")
        ;
    }

    // 增加JWT的支持
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                .accessTokenConverter(tokenConverter())
                .authenticationManager(this.authenticationManager)
                // 重用刷新令牌的token
                .reuseRefreshTokens(true);
    }

    @Bean
    JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenConverter());
    }

    @Bean
    JwtAccessTokenConverter tokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        // ClassPathResource用于读取类路径下面的资源文件
        // "123456"是生成【密钥库】的时候提供的密码，-storepass参数的值
        KeyStoreKeyFactory keyStoreKeyFactory
                = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"),
                "123456".toCharArray());
        // 123456 是-keypass的值
        KeyPair keyPair = keyStoreKeyFactory
                .getKeyPair("fk", "123456".toCharArray());

        converter.setKeyPair(keyPair);
        return converter;
    }
}
