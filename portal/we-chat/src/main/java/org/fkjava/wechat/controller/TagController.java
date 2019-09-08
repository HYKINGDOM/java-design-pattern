package org.fkjava.wechat.controller;

import org.fkjava.commons.domain.Result;
import org.fkjava.wechat.domain.Tag;
import org.fkjava.wechat.service.WeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private WeiXinService weiXinService;

    @GetMapping("{account}")
    public Page<Tag> allTags(
            @PathVariable("account") String account,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "keyword", required = false) String keyword
    ) {
        return this.weiXinService.findTags(account, pageNumber, keyword);
    }

    @PostMapping
    public Result save(@RequestBody Tag tag) {
        return this.weiXinService.saveTag(tag);
    }
}
