package org.fkjava.auth.wechat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.fkjava.user.api.RemoteUserService;
import org.fkjava.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DelegatingOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class WeChatOAuth2UserService extends DelegatingOAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RemoteUserService userService;
    private final List<OAuth2UserService<OAuth2UserRequest, OAuth2User>> userServices;

    public WeChatOAuth2UserService(RemoteUserService userService, List<OAuth2UserService<OAuth2UserRequest, OAuth2User>> oAuth2UserServices) {
        super(oAuth2UserServices);
        this.userService = userService;
        this.userServices = oAuth2UserServices;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");

        // 先从远程获取用户信息，然后根据OpenID到本地数据库获取用户的权限信息。
        // 如果用户在本地数据库中没有信息，则保存用户信息到数据库，并跟user-center中的用户信息关联起来（相当于关注）。
        String openId = (String) userRequest.getAdditionalParameters().get("openid");
        User user = userService.byOpenId(openId);
        if (user == null) {
            // 查询远程用户信息
            OAuth2User oAuth2User = this.userServices.stream()
                    .map(userService -> userService.loadUser(userRequest))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
            log.debug("微信新用户，公众号平台返回的用户信息: \n{}", oAuth2User);

            String name = (String) oAuth2User.getAttributes().get("nickname");

            user = new User();
            user.setName(name);
            user.setLoginName(openId);
            user.setOpenId(openId);
            user.setAccountNonLocked(true);
            user.setEnabled(true);
            // 注册新用户
            user = userService.registry(user);
        }

        Collection<? extends GrantedAuthority> authorities =
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleKey()))
                        .collect(Collectors.toList());

        Map<String, Object> attributes = objectMapper.convertValue(user, HashMap.class);
        attributes = new HashMap<>(attributes);
        attributes.remove("password");
        String nameAttributeKey = "id";
        DefaultOAuth2User oAuth2User = new DefaultOAuth2User(authorities, attributes, nameAttributeKey);

        log.debug("微信OAuth 2.0授权用户: \n{}", oAuth2User);
        return oAuth2User;
    }
}
