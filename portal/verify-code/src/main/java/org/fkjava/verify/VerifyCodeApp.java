package org.fkjava.verify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VerifyCodeApp {
    public static void main(String[] args) {
        SpringApplication.run(VerifyCodeApp.class, args);
    }
}
