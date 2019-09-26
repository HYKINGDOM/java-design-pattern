package org.fkjava.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.fkjava.commons.config.CommonConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationCodeAuthenticationTokenConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.web.server.WebFilter;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(CommonConfigProperties.class)
@Slf4j
public class SecurityConfig {

    private final CommonConfigProperties properties;
    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(CommonConfigProperties properties, ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.properties = properties;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // 排除不需要权限的路径，主要是登录地址、验证码等
        http.authorizeExchange()
                .pathMatchers(
                        "/favicon.ico",
                        "/oauth-server/oauth/token_key",
                        "/oauth-server/oauth/authorize",
                        "/oauth-server/oauth/token",
                        "/oauth-server/uc/login",
                        "/oauth-server/uc/do-login",
                        "/oauth-server/uc/do-logout",
                        "/user-center/user/user-info",
                        "/verify-code/**",
                        "/oauth-server/wechat/oauth2/authorization/**",
                        "/oauth-server/wechat/oauth2/authorization",
                        "/oauth-server/wechat/login/oauth2/code/**",
                        "/oauth-server/wechat/login/**",
                        "/we-chat/receiver",
                        "/*/actuator/**",
                        "/actuator/**"
                )
                .permitAll();
        // 其他路径都需要授权
        http.authorizeExchange().anyExchange().authenticated();
        // 激活OAuth 2.0的登录
        http.oauth2Login()
                .authenticationConverter(this.serverOAuth2AuthorizationCodeAuthenticationTokenConverter());

        // 退出登录
        RedirectServerLogoutSuccessHandler redirectServerLogoutSuccessHandler
                = new RedirectServerLogoutSuccessHandler();
        redirectServerLogoutSuccessHandler.setLogoutSuccessUrl(URI.create(properties.getApiPrefix() + "/oauth-server/uc/do-logout"));
        http.logout()
                // 退出登录的地址，需要加上/api
                .logoutUrl("/logout")
                // 需要到鉴权中心去退出登录
                .logoutSuccessHandler(redirectServerLogoutSuccessHandler);
        // 禁用CSRF（防止跨站攻击）
        http.csrf().disable();
        return http.build();
    }

    // 访问网关的时候，都通过/api作为前缀来访问，但是网关内部注册的路由并且/api作为前缀
    @Bean
    public WebFilter apiWebFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String rawPath = request.getURI().getRawPath();
            log.trace("原始路径: {}", rawPath);
            if (rawPath.startsWith(properties.getApiPath())) {
                // 如果/api开头，那么就把/api去掉
                String newPath = rawPath.substring(properties.getApiPath().length());
                log.trace("处理后的路径: {}", newPath);
                // 构建新的请求，把新的路径用于访问服务路由
                ServerHttpRequest newRequest = request.mutate().path(newPath).build();
                exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newPath);
                // 使用新的路径访问，会自动根据服务路由去匹配路径
                return chain.filter(exchange.mutate().request(newRequest).build());
            } else {
                log.trace("原路执行");
                return chain.filter(exchange);
            }
        };
    }

    private ServerOAuth2AuthorizationCodeAuthenticationTokenConverter serverOAuth2AuthorizationCodeAuthenticationTokenConverter() {
        return new org.fkjava.gateway.oauth2.ServerOAuth2AuthorizationCodeAuthenticationTokenConverter(clientRegistrationRepository);
    }
}
