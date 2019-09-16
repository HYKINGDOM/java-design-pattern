package org.fkjava.user;

import org.fkjava.resource.config.OAuth2ResourceConfig;
import org.fkjava.verify.api.VerifyCodeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
// 由于需要重写配置，所有前面自定义的OAuth2ResourceConfig不要import，而是继承
//@Import(OAuth2ResourceConfig.class)
// 激活资源服务，如果是把OAuth2ResourceConfig类的配置import进来，则不需要再写此注解
@EnableResourceServer
// 所有的服务注册到注册中心以后，都可以被各个服务发现
// 此注解是Spring Cloud里面通用的服务发现方式，不管是哪种注册中心，都可以实现服务发现
@EnableDiscoveryClient
// 这种仅用于Eureka作为注册中心的时候使用的服务发现
//@EnableEurekaClient
// 调用远程服务，必须依赖openfeign
@EnableFeignClients(clients = VerifyCodeService.class)
// 由于user-center里面的User相关的类，放在user-center-domain包里面，如果需要得到实体里面需要扫描！
@EntityScan("org.fkjava")
@EnableGlobalMethodSecurity(jsr250Enabled = true, securedEnabled = true, prePostEnabled = true)
public class UserCenterApp
        // 继承OAuth2ResourceConfig其实就是为了得到OAuth 2资源服务器的配置
        extends OAuth2ResourceConfig {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 继承OAuth2ResourceConfig的目的就是为了把不需要授权的URL排除掉
        http.authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers(
                        "/user/byLoginName",
                        "/user/byOpenId",
                        "/user/byId",
                        "/user/byPhone",
                        "/user/registry"
                )
                .permitAll()
                .anyRequest().authenticated();
    }

    // 配置密码加密程序，必须跟登录、授权服务的密码加密程序保持一致！
    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> passwordEncoderMap = new HashMap<>();
        passwordEncoderMap.put("bcrypt", new BCryptPasswordEncoder());
        passwordEncoderMap.put("scrypt", new SCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(
                "bcrypt",
                passwordEncoderMap);
    }


    public static void main(String[] args) {
        SpringApplication.run(UserCenterApp.class, args);
    }
}
