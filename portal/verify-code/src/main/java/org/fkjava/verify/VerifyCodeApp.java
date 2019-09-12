package org.fkjava.verify;

import org.fkjava.commons.config.ShortMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(ShortMessage.class)
public class VerifyCodeApp {
    public static void main(String[] args) {
        SpringApplication.run(VerifyCodeApp.class, args);
    }
}
