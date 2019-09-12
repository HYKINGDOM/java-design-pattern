package org.fkjava.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
// 激活配置中心服务器
@EnableConfigServer
public class ConfigApp {

    ConfigFileApplicationListener d;

    public static void main(String[] args) {
        SpringApplication.run(ConfigApp.class, args);
    }
}
