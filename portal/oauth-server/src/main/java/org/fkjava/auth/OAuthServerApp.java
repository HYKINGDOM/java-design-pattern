package org.fkjava.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("org.fkjava")
public class OAuthServerApp {
    public static void main(String[] args) {
        SpringApplication.run(OAuthServerApp.class, args);
    }
}
