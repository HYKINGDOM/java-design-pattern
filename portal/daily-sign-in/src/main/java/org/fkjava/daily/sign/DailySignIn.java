package org.fkjava.daily.sign;

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
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
@Import(OAuth2ResourceConfig.class)
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
public class DailySignIn {

    public static void main(String[] args) {
        SpringApplication.run(DailySignIn.class, args);
    }
}
