package org.fkjava.wechat.controller;

import org.fkjava.commons.domain.Result;
import org.fkjava.wechat.domain.Tag;
import org.fkjava.wechat.domain.UserInfo;
import org.fkjava.wechat.service.WeiXinService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserInfoController {

    private final WeiXinService weiXinService;

    public UserInfoController(WeiXinService weiXinService) {
        this.weiXinService = weiXinService;
    }

    @GetMapping("/names/{account}")
    public Page<UserInfo> allUserNames(
            @PathVariable("account") String account,
            @RequestParam(name = "pn", defaultValue = "0") int pageNumber,
            @RequestParam(name = "ps", defaultValue = "1000") int pageSize,
            @RequestParam(name = "kw", required = false) String keyword
    ) {
        return this.weiXinService.findUserNames(account, pageNumber, pageSize, keyword);
    }

    @GetMapping("users/{account}")
    public Page<UserInfo> allUsers(
            @PathVariable("account") String account,
            @RequestParam(name = "pn", defaultValue = "0") int pageNumber,
            @RequestParam(name = "ps", defaultValue = "10") int pageSize,
            @RequestParam(name = "kw", required = false) String keyword
    ) {
        return this.weiXinService.findUsers(account, pageNumber, pageSize, keyword);
    }

    @GetMapping("{openId}")
    public UserInfo findUserByOpenId(
            @PathVariable String openId) {
        return this.weiXinService.findUserById(openId);
    }

    @PostMapping("update-remark")
    public Result updateRemark(String id, String remark) {
       return this.weiXinService.updateRemark(id, remark);
    }

    @GetMapping("tags/{id}")
    public List<Tag> findTagsByUserId(@PathVariable String id){
        return this.weiXinService.findTagsByUserId(id);
    }

    @PostMapping("update-tags")
    public Result updateTags(String id, String[] tagId) {
        return this.weiXinService.updateTags(id, tagId);
    }
}
