package org.fkjava.test;

import org.fkjava.resource.config.OAuth2ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
// 如果要自定义某个路径不要权限，那么继承OAuth2ResourceConfig，重写配置方法
// 如果不需要自定义，则直接import即可
// 注意：如果是继承的，那么必须自己写@EnableResourceServer注解
@Import(OAuth2ResourceConfig.class)
public class TestApp {
    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }
}
