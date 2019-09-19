package org.fkjava.auth.wechat.config;

import lombok.extern.slf4j.Slf4j;
import org.fkjava.auth.wechat.converter.JacksonMessageConverter;
import org.fkjava.auth.wechat.converter.OAuth2AccessTokenResponseConverter;
import org.fkjava.auth.wechat.converter.WeChatOAuth2UserRequestEntityConverter;
import org.fkjava.auth.wechat.service.WeChatOAuth2LoginAuthenticationProvider;
import org.fkjava.auth.wechat.service.WeChatOAuth2UserService;
import org.fkjava.commons.config.CommonConfigProperties;
import org.fkjava.user.api.RemoteUserService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class WeChatLoginConfig {

    private final String baseUri = "/wechat" + OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
    private String apiPrefix;
    private String apiPath;
    private String servicePath;
    private RemoteUserService userService;
    private CommonConfigProperties properties;

    public WeChatLoginConfig(
            RemoteUserService userService,
            CommonConfigProperties properties
    ) {
        this.apiPath = properties.getApiPath();
        this.apiPrefix = properties.getApiPrefix();
        CommonConfigProperties.WeChatLogin login = properties.getWeChatLogin();
        this.servicePath = login.getServicePath();
        this.userService = userService;
        this.properties = properties;
    }

    public void configure(HttpSecurity http) throws Exception {

        ClientRegistrationRepository clientRegistrationRepository
                = clientRegistrationRepository();

        http
                .oauth2Login()
                .clientRegistrationRepository(clientRegistrationRepository)
                .authorizationEndpoint()
                .authorizationRequestResolver(oAuth2AuthorizationRequestResolver(clientRegistrationRepository))
                .baseUri(baseUri)
                .and().loginProcessingUrl(properties.getWeChatLogin().getLoginProcessingUrl())
                .and().authenticationProvider(oAuth2LoginAuthenticationProvider())
        ;
    }

    private OAuth2LoginAuthenticationProvider oAuth2LoginAuthenticationProvider() {
        OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeTokenResponseClient
                = authorizationCodeTokenResponseClient();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = oAuth2UserService();
        return new WeChatOAuth2LoginAuthenticationProvider(
                authorizationCodeTokenResponseClient,
                oAuth2UserService,
                apiPath,
                servicePath);
    }

    private InMemoryClientRegistrationRepository clientRegistrationRepository() {

        String redirectUriTemplate = properties.getWeChatLogin().getRedirectUri();
        if (!redirectUriTemplate.startsWith("http://") && !redirectUriTemplate.startsWith("https://")) {
            redirectUriTemplate = this.apiPrefix + redirectUriTemplate;
        }
        final String redirectUri = redirectUriTemplate;


        List<ClientRegistration> registrations = properties.getAllWeChats().stream()
                .map(weChat -> {
                    String appId = weChat.getAppId();
                    String appSecret = weChat.getAppSecret();

                    return ClientRegistration.withRegistrationId(weChat.getAccount())
                            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                            .authorizationUri("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId)
                            .clientId(appId)
                            .clientSecret(appSecret)
                            .redirectUriTemplate(redirectUri)
                            .tokenUri("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret)
                            .scope("snsapi_userinfo")
                            .userInfoUri("https://api.weixin.qq.com/sns/userinfo")
                            .userNameAttributeName("openid")
                            .userInfoAuthenticationMethod(AuthenticationMethod.QUERY)
                            .build();
                })
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private DefaultOAuth2AuthorizationRequestResolver oAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        return new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, baseUri);
    }

    private RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
                new FormHttpMessageConverter(), accessTokenResponseHttpMessageConverter(), messageConverter()));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        return restTemplate;
    }

    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeTokenResponseClient() {

        DefaultAuthorizationCodeTokenResponseClient client
                = new DefaultAuthorizationCodeTokenResponseClient();
        client.setRestOperations(restTemplate());

        return client;
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = this.oAuth2UserRequestEntityConverter();
        RestOperations restOperations = this.restTemplate();

        DefaultOAuth2UserService service = new DefaultOAuth2UserService();
        service.setRestOperations(restOperations);
        service.setRequestEntityConverter(requestEntityConverter);

        return new WeChatOAuth2UserService(userService, List.of(service));
    }

    private JacksonMessageConverter messageConverter() {
        // 负责解析微信公众号平台返回的JSON字符串，比如用户信息
        return new JacksonMessageConverter();
    }

    private OAuth2AccessTokenResponseHttpMessageConverter accessTokenResponseHttpMessageConverter() {
        // 解析微信公众号平台返回的访问令牌
        return new OAuth2AccessTokenResponseConverter();
    }

    private OAuth2UserRequestEntityConverter oAuth2UserRequestEntityConverter() {
        return new WeChatOAuth2UserRequestEntityConverter();
    }
}
