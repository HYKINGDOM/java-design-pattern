package org.fkjava.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.web.server.WebFilter;

import java.util.List;

// 目前没有作用，因为目前没有跨域，所有请求都是通过前端代理 -> 网关代理 -> 后端服务
// 跨域：前端没有代理，直接在前端服务器里面访问网关代理
// 当前端和网关的访问IP和端口不同的时候就是跨域
@Configuration
public class CorsConfig {

    // 激活跨域访问
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    WebFilter corsWebFilterChain() {
        WebFilter filter = (exchange, chain) -> {
            // 允许任意源跨域访问
            exchange.getResponse()
                    .getHeaders()
                    // 哪些源可以跨域访问网关
                    // 比如 127.0.0.1:8080
                    // 比如 *.fkjava.org
                    .setAccessControlAllowOrigin("*");
            // 在响应头里面加上允许跨域访问的响应头
            exchange.getResponse()
                    .getHeaders()
                    .setAccessControlAllowCredentials(true);
            // 运行哪些类型的请求跨域访问
            exchange.getResponse()
                    .getHeaders()
                    .setAccessControlAllowMethods(
                            List.of(
                                    HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE,
                                    HttpMethod.PUT, HttpMethod.OPTIONS, HttpMethod.TRACE, HttpMethod.PATCH,
                                    HttpMethod.HEAD
                            ));
            // 跨域请求的最长时间
            exchange.getResponse()
                    .getHeaders()
                    .setAccessControlMaxAge(3600);
            // 有哪些请求头的时候，允许跨域
            exchange.getResponse()
                    .getHeaders()
                    .setAccessControlAllowHeaders(
                            List.of(
                                    "Origin",
                                    "X-Requested-With",
                                    "Content-Type",
                                    "Accept"
                            )
                    );
            return chain.filter(exchange);
        };
        return filter;
    }
}
