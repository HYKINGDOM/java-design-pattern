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

    @GetMapping("/names/{account}")
    public Page<UserInfo> allUserNames(
            @PathVariable("account") String account,
            @RequestParam(name = "pn", defaultValue = "0") int pageNumber,
            @RequestParam(name = "ps", defaultValue = "1000") int pageSize,
            @RequestParam(name = "kw", required = false) String keyword
    ) {
        return this.weiXinService.findUserNames(account, pageNumber, pageSize, keyword);
    }

    @GetMapping("{account}")
    public Page<UserInfo> allUsers(
            @PathVariable("account") String account,
            @RequestParam(name = "pn", defaultValue = "0") int pageNumber,
            @RequestParam(name = "ps", defaultValue = "10") int pageSize,
            @RequestParam(name = "kw", required = false) String keyword
    ) {
        return this.weiXinService.findUsers(account, pageNumber, pageSize, keyword);
    }
}
