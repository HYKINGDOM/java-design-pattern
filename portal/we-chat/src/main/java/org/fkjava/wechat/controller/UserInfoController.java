package org.fkjava.wechat.controller;

import org.fkjava.wechat.domain.UserInfo;
import org.fkjava.wechat.service.WeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private WeiXinService weiXinService;

    @GetMapping("{account}")
    public Page<UserInfo> allTags(
            @PathVariable("account") String account,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "keyword", required = false) String keyword
    ) {
        return this.weiXinService.findUsers(account, pageNumber, keyword);
    }
}
