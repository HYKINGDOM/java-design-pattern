package org.fkjava.auth.wechat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Slf4j
public class WeChatOAuth2LoginAuthenticationProvider extends OAuth2LoginAuthenticationProvider {

    private static final String INVALID_STATE_PARAMETER_ERROR_CODE = "invalid_state_parameter";
    private static final String INVALID_REDIRECT_URI_PARAMETER_ERROR_CODE = "invalid_redirect_uri_parameter";

    private GrantedAuthoritiesMapper authoritiesMapper = (authorities -> authorities);
    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeTokenResponseClient;
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
    private String apiPath;
    private String servicePath;

    /**
     * Constructs an {@code OAuth2LoginAuthenticationProvider} using the provided parameters.
     *
     * @param accessTokenResponseClient the client used for requesting the access token credential from the Token Endpoint
     * @param oAuth2UserService         the service used for obtaining the user attributes of the End-User from the UserInfo Endpoint
     */
    public WeChatOAuth2LoginAuthenticationProvider(
            OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService,
            String apiPath,
            String servicePath) {
        super(accessTokenResponseClient, oAuth2UserService);
        this.authorizationCodeTokenResponseClient = accessTokenResponseClient;
        this.oAuth2UserService = oAuth2UserService;
        this.apiPath = apiPath;
        this.servicePath = servicePath;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        try {
            OAuth2LoginAuthenticationToken authorizationCodeAuthentication =
                    (OAuth2LoginAuthenticationToken) authentication;

            if (authorizationCodeAuthentication.getAuthorizationExchange()
                    .getAuthorizationRequest().getScopes().contains("openid")) {
                return null;
            }

            // 获取微信公众号平台的访问令牌
            OAuth2AccessTokenResponse accessTokenResponse;
            try {
                validate(authorizationCodeAuthentication.getAuthorizationExchange());

                accessTokenResponse = authorizationCodeTokenResponseClient.getTokenResponse(
                        new OAuth2AuthorizationCodeGrantRequest(
                                authorizationCodeAuthentication.getClientRegistration(),
                                authorizationCodeAuthentication.getAuthorizationExchange()));

            } catch (OAuth2AuthorizationException ex) {
                log.error("获取微信公众号访问令牌出现问题 : " + ex.getMessage(), ex);
                OAuth2Error oauth2Error = ex.getError();
                throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
            }

            OAuth2AccessToken accessToken = accessTokenResponse.getAccessToken();
            Map<String, Object> additionalParameters = accessTokenResponse.getAdditionalParameters();

            // 从微信公众号加载用户信息
            OAuth2User oauth2User = oAuth2UserService.loadUser(new OAuth2UserRequest(
                    authorizationCodeAuthentication.getClientRegistration(), accessToken, additionalParameters));

            Collection<? extends GrantedAuthority> mappedAuthorities =
                    authoritiesMapper.mapAuthorities(oauth2User.getAuthorities());

            OAuth2LoginAuthenticationToken authenticationResult = new OAuth2LoginAuthenticationToken(
                    authorizationCodeAuthentication.getClientRegistration(),
                    authorizationCodeAuthentication.getAuthorizationExchange(),
                    oauth2User,
                    mappedAuthorities,
                    accessToken,
                    accessTokenResponse.getRefreshToken());
            authenticationResult.setDetails(authorizationCodeAuthentication.getDetails());

            return authenticationResult;
        } catch (Exception ex) {
            log.error("微信公众号用户授权失败 : " + ex.getMessage(), ex);
            throw new RuntimeException("微信公众号用户授权失败 : " + ex.getLocalizedMessage(), ex);
        }
    }

    private void validate(OAuth2AuthorizationExchange authorizationExchange) {
        OAuth2AuthorizationRequest authorizationRequest = authorizationExchange.getAuthorizationRequest();
        OAuth2AuthorizationResponse authorizationResponse = authorizationExchange.getAuthorizationResponse();

        if (authorizationResponse.statusError()) {
            throw new OAuth2AuthorizationException(authorizationResponse.getError());
        }

        if (!authorizationResponse.getState().equals(authorizationRequest.getState())) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthorizationException(oauth2Error);
        }

        String responseUri = authorizationResponse.getRedirectUri();
        log.trace("原始响应URI: {}", responseUri);
        responseUri = responseUri.substring(responseUri.indexOf("://") + 3);
        responseUri = responseUri.substring(responseUri.indexOf("/"));
        log.trace("截取后的响应URI: {}", responseUri);


        String requestUri = authorizationRequest.getRedirectUri();
        log.trace("原始请求URI: {}", requestUri);
        requestUri = requestUri.substring(requestUri.indexOf("://") + 3);
        requestUri = requestUri.substring(requestUri.indexOf("/"));
        // 去掉/api部分
        if (requestUri.startsWith(apiPath)) {
            requestUri = requestUri.substring(apiPath.length());
        }
        // 去掉/oauth-server/部分
        if (requestUri.startsWith(servicePath)) {
            requestUri = requestUri.substring(servicePath.length());
        }
        log.trace("截取后的请求URI: {}", requestUri);

        if (!responseUri.equals(requestUri)) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_REDIRECT_URI_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthorizationException(oauth2Error);
        }
    }
}
