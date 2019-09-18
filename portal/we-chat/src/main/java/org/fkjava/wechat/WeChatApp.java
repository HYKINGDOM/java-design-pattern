package org.fkjava.wechat;

import org.fkjava.commons.config.CommonConfigProperties;
import org.fkjava.resource.config.OAuth2ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableEurekaClient
//@Import(OAuth2ResourceConfig.class)
@EnableResourceServer
@EnableFeignClients("org.fkjava")
@EntityScan("org.fkjava")
@EnableConfigurationProperties(CommonConfigProperties.class)
// 排除掉某个类或它的子类，不要扫描
@EnableJpaRepositories(excludeFilters = {
        @ComponentScan.Filter(classes = KeyValueRepository.class, type = FilterType.ASSIGNABLE_TYPE)
})
// 只扫描指定的类或者它的子类
@EnableRedisRepositories(includeFilters = {
        @ComponentScan.Filter(classes = KeyValueRepository.class, type = FilterType.ASSIGNABLE_TYPE)
})
@EnableAsync
public class WeChatApp extends OAuth2ResourceConfig {

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        http.oauth2ResourceServer().jwt();
        http.authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers(
                        "/**/*.png",
                        "/receiver"
                )
                .permitAll()
                .anyRequest().authenticated();
    }

    public static void main(String[] args) {
        SpringApplication.run(WeChatApp.class, args);
    }
}
