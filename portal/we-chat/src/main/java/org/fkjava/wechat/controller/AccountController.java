package org.fkjava.wechat.controller;

import org.fkjava.commons.config.CommonConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// 微信公众号数据接口
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private CommonConfigProperties commonConfigProperties;

    private Function<CommonConfigProperties.WeChat, Map<String, String>> map = weChat -> {
        String text = weChat.getName() == null ? weChat.getAccount() : weChat.getName() + "(" + weChat.getAccount() + ")";
        String account = weChat.getAccount();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("title", text);
        map.put("key", account);
        return map;
    };

    @GetMapping
    public List<Map<String, String>> getAccounts() {
        return commonConfigProperties.getAllWeChats()
                .stream()
                .map(map).collect(Collectors.toList());
    }

    @GetMapping("{account}")
    public Map<String, String> getAccount(@PathVariable String account) {
        return commonConfigProperties.getAllWeChats()
                .stream()
                .filter(weChat -> weChat.getAccount().equals(account))
                .map(map)
                .findFirst().orElse(null);
    }
}
