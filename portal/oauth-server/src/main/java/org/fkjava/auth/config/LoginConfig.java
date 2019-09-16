package org.fkjava.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.fkjava.auth.wechat.config.WeChatLoginConfig;
import org.fkjava.commons.config.CommonConfigProperties;
import org.fkjava.user.api.RemoteUserService;
import org.fkjava.user.domain.User;
import org.fkjava.user.vo.AuthUserDetails;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Configuration
// 一个项目、一个进程中，只能有一个使用@EnableWebSecurity注解的类。
// 如果真的要写多个，那么就必须结合@Order注解，只能不同的@EnableWebSecurity类的顺序。
@EnableWebSecurity
@Order(100)//默认值是100，只要数字不同，即可有多个@EnableWebSecurity
// 激活自定义的配置
@EnableConfigurationProperties(CommonConfigProperties.class)
@Slf4j
public class LoginConfig extends WebSecurityConfigurerAdapter {

    //    private Logger log = LoggerFactory.getLogger(LoginConfig.class);
    private final RemoteUserService userService;
    private final CommonConfigProperties commonConfigProperties;

    public LoginConfig(RemoteUserService userService, CommonConfigProperties commonConfigProperties) {
        this.userService = userService;
        this.commonConfigProperties = commonConfigProperties;
    }

    // 配置跟HTTP协议关系不大的一些配置，比如忽略哪些URL不要验证权限
    @Override
    public void configure(WebSecurity web) {
        // 忽略哪些匹配的路径不要验证权限
        web.ignoring().mvcMatchers(
                "/favicon.ico",
                "/resources/**",
                "/webjars/**",
                "/images/**",
                "/error",
                "/hystrix.stream"
        )
                .regexMatchers("\\.ico$", "\\.jpg$", "\\.jpeg$", "\\.png$", "\\.gif$")
        ;
    }

    // 配置跟HTTP相关的信息，比如登录界面、URL的权限等
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler
                = this.successHandler(
                commonConfigProperties.getHostPrefix()
        );
        RequestCache requestCache = http.getSharedObject(RequestCache.class);
        if (requestCache == null) {
            http.setSharedObject(RequestCache.class, requestCache());
//            http.setSharedObject(RequestCache.class, new HttpSessionRequestCache());
            requestCache = http.getSharedObject(RequestCache.class);
        }
        successHandler.setRequestCache(requestCache);


        http
                .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers(
                        "/wechat/oauth2/authorization/**",
//                        "/wechat/login/wechat",
                        "/wechat/login/oauth2/code/**").permitAll()
                // 访问/uc/user-info的时候，必须具有USER身份
                .antMatchers("/uc/user-info").hasRole("USER")//
                // 访问其他任意请求，必须已经授权，至于什么身份则不考虑
                .anyRequest().authenticated()//
                .and()//
                .httpBasic().disable()
                .formLogin()//
                // 登录表单的用户名的输入框名称
//                .usernameParameter("username")//

                // 为了根据不同的请求场景，返回不同的数据给客户端。
                // 比如浏览器直接访问的时候，应该返回一个HTML界面；
                // 如果是前后端分离，本身是AJAX请求，则应该返回JSON数据，并且状态码应该是401。
                .loginPage("/uc/login")//

                // 处理登录，不管是浏览器直接请求，还是AJAX请求都一样。
                .loginProcessingUrl("/uc/do-login")//

                // 登录成功以后执行的操作
                .successHandler(successHandler)//
                // 登录失败时执行的操作
                .failureHandler(failureHandler())//
                .permitAll()// 所有人可以直接访问，不需要权限
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()//
                .logout()//
                // 退出登录的时候，如果激活了csrf，那么必须使用POST请求才能退出登录
                .logoutUrl("/uc/do-logout")//
                .logoutSuccessUrl(commonConfigProperties.getApiPrefix() + "/oauth-server/uc/login")
                .clearAuthentication(true)//
                // 跨域访问
                .and().cors()//
                // 防止跨站攻击
                .and().csrf().disable()
        ;

        // 实现微信登录的配置
        WeChatLoginConfig weChatLoginConfig = new WeChatLoginConfig(
                userService,
                commonConfigProperties
        );
        weChatLoginConfig.configure(http);
    }

    // 构建自定义的授权管理器，主要包含权限认证的方式和逻辑
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsService userDetailsService = userDetailsService();

        // 使用@Bean注解的方法，在Spring容器里面只会被调用一次。
        // 如果有多次调用，实际上返回的值是缓存的值。
        // 只要对象里面有@Bean注解，对象就会产生代理对象。
        PasswordEncoder passwordEncoder = passwordEncoder();
        auth
                // 设置查询用户的服务
                .userDetailsService(userDetailsService)
                // 设置密码的加密方案
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // 调用远程（user-center）服务里面的方法（对应控制器），得到用户信息
            User user = this.userService.byLoginName(username);

            if (user == null) {
                // 登录名没有找到用户，则尝试手机号码登录
                user = this.userService.byPhone(username);
            }

            if (user == null) {
                throw new UsernameNotFoundException("用户 [" + username + "] 不存在");
            }

            List<GrantedAuthority> authorities = new LinkedList<>();

            // 把角色转换为权限，需要固定在角色的key之前加上ROLE_。
            user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleKey())));
            user.getRoles().stream()
                    .filter(role -> role.getRoleKey().equals("USER"))
                    .findFirst()
                    .ifPresentOrElse(role -> {
                    }, () -> {
                        // 没有USER角色，则强行加上一个，保证用户最少觉有一个角色
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    });

            AuthUserDetails ud = new AuthUserDetails(
                    user.getId(),
                    user.getName(),
                    user.getLoginName(),
                    user.getPassword(),
                    user.getPhone(),
                    user.getTenantId(),
                    user.getOpenId(),
                    user.getRegisteredTime(),
                    user.getPasswordExpireTime(),
                    user.getAccountExpireTime(),
                    user.isEnabled(),
                    user.isAccountNonLocked(),
                    authorities);
            log.debug("账号/密码授权用户: \n{}", ud);
            return ud;
        };
    }

    // 自定义登录成功的处理器，根据不同的请求参数返回不同的结果
    // defaultSuccessUrl表示登录成功后，默认的响应地址。但是注意：如果因为之前是访问某个URL而重定向到登录的，需要返回原页面。
    private SavedRequestAwareAuthenticationSuccessHandler successHandler(
            String defaultSuccessUrl) {

        // SavedRequestAwareAuthenticationSuccessHandler本身就是可以保存之前请求信息的对象
        SavedRequestAwareAuthenticationSuccessHandler handler;
        handler = new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Authentication authentication
            ) throws ServletException, IOException {

                // 获取请求头，得到其中的Accept头，如果是application/json开头，返回JSON
                String accept = request.getHeader("accept");
                if (!StringUtils.isEmpty(accept) && accept.toLowerCase().indexOf("application/json") == 0) {
                    log.debug("登录成功，返回JSON");
                    sendJson(1, "登录成功", request, response);
                } else {
                    log.debug("登录成功，默认返回");
                    super.onAuthenticationSuccess(request, response, authentication);
                }
            }
        };
        handler.setDefaultTargetUrl(defaultSuccessUrl);
        handler.setAlwaysUseDefaultTargetUrl(false);
        return handler;
    }

    private AuthenticationFailureHandler failureHandler() {
        return new ForwardAuthenticationFailureHandler("/uc/login?error") {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                String accept = request.getHeader("accept");
                boolean isXhr = request.getHeader("XMLHttpRequest") != null;
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                if (isXhr || !StringUtils.isEmpty(accept) && accept.toLowerCase().indexOf("application/json") == 0) {
                    log.debug("登录失败，返回JSON");
                    sendJson(2, "登录失败", request, response);
                } else {
                    log.debug("登录失败，默认返回");
                    super.onAuthenticationFailure(request, response, exception);
                }
            }
        };
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint(commonConfigProperties.getApiPrefix() + "/oauth-server/uc/login") {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
                    throws IOException, ServletException {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                if (request.getHeader("Accept") != null
                        && request.getHeader("Accept").contains("application/json")) {
                    log.debug("访问{}时未登录，返回JSON", request.getRequestURI());
                    sendJson(401, "未登录", request, response);
                } else {
                    log.debug("访问{}时未登录，默认返回", request.getRequestURI());
                    super.commence(request, response, authException);
                }
            }
        };
    }

    private void sendJson(int code, String message, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 返回JSON
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Request-URI", request.getRequestURI());
        response.setCharacterEncoding("UTF-8");

        response.getWriter().println(json);
    }

    // 暴露鉴权服务为一个Bean
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public HttpSessionRequestCache requestCache() {
        return new HttpSessionRequestCache() {

            static final String SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";
            private RequestMatcher requestMatcher = AnyRequestMatcher.INSTANCE;
            private String sessionAttrName = SAVED_REQUEST;
            private boolean createSessionAllowed = true;

            @Override
            public void setSessionAttrName(String sessionAttrName) {
                this.sessionAttrName = sessionAttrName;
                super.setSessionAttrName(sessionAttrName);
            }

            @Override
            public void setCreateSessionAllowed(boolean createSessionAllowed) {
                this.createSessionAllowed = createSessionAllowed;
                super.setCreateSessionAllowed(createSessionAllowed);
            }

            public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
                if (requestMatcher.matches(request)) {
                    String url = commonConfigProperties.getApiPrefix() +
                            "/oauth-server" + request.getRequestURI() + "?" + request.getQueryString();
                    log.debug("保存请求，之前访问的路径：{}", url);
                    SimpleSavedRequest savedRequest = new SimpleSavedRequest(url);

                    if (createSessionAllowed || request.getSession(false) != null) {
                        // Store the HTTP request itself. Used by
                        // AbstractAuthenticationProcessingFilter
                        // for redirection after successful authentication (SEC-29)
                        request.getSession().setAttribute(this.sessionAttrName, savedRequest);
                        logger.debug("DefaultSavedRequest added to Session: " + savedRequest);
                    }
                } else {
                    logger.debug("Request not saved as configured RequestMatcher did not match");
                }
            }
        };
    }
}
