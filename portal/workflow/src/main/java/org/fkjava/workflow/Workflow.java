package org.fkjava.workflow;

import org.fkjava.resource.config.OAuth2ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(OAuth2ResourceConfig.class)
@EnableDiscoveryClient
public class Workflow {
    public static void main(String[] args) {
        SpringApplication.run(Workflow.class, args);
    }
}
