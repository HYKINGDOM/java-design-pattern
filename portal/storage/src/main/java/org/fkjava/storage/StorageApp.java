package org.fkjava.storage;

import org.fkjava.commons.config.CommonConfigProperties;
import org.fkjava.resource.config.OAuth2ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableEurekaClient
@Import(OAuth2ResourceConfig.class)
@EnableConfigurationProperties(CommonConfigProperties.class)
public class StorageApp {

    public static void main(String[] args) {
        SpringApplication.run(StorageApp.class, args);
    }
}
