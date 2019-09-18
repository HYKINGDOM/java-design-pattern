package org.fkjava.gift;

import org.fkjava.resource.config.OAuth2ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

// 表示该应用是一个Spring Boot应用
@SpringBootApplication
// 自动把当前服务注册到注册中心
@EnableEurekaClient
// 导入授权相关的配置，在没有登录的时候会自动跳转到登录页面，OAuth2ResourceConfig是在resource-auth模块米
@Import(OAuth2ResourceConfig.class)
public class GiftApp {

    public static void main(String[] args) {
        SpringApplication.run(GiftApp.class, args);
    }
}
