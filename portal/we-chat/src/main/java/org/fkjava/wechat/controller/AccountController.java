package org.fkjava.wechat.controller;

import org.fkjava.commons.config.CommonConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 微信公众号数据接口
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private CommonConfigProperties commonConfigProperties;

    @GetMapping
    public List<Map<String, String>> getAccounts() {
        return commonConfigProperties.getAllWeChats()
                .stream()
                .map(weChat -> {
                    String text = weChat.getName() == null ? weChat.getAccount() : weChat.getName() + "(" + weChat.getAccount() + ")";
                    String account = weChat.getAccount();
                    Map<String, String> map = new LinkedHashMap<>();
                    map.put("title", text);
                    map.put("key", account);
                    return map;
                }).collect(Collectors.toList());
    }
}
