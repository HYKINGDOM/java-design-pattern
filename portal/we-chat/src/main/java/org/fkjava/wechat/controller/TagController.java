package org.fkjava.wechat.controller;

import org.fkjava.commons.domain.Result;
import org.fkjava.wechat.domain.Tag;
import org.fkjava.wechat.service.WeiXinService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tag")
public class TagController {

    private final WeiXinService weiXinService;

    public TagController(WeiXinService weiXinService) {
        this.weiXinService = weiXinService;
    }

    @GetMapping("{account}")
    public Page<Tag> allTags(
            @PathVariable("account") String account,
            @RequestParam(name = "pn", defaultValue = "0") int pageNumber,
            @RequestParam(name = "ps", defaultValue = "100") int pageSize,
            @RequestParam(name = "kw", required = false) String keyword
    ) {
        return this.weiXinService.findTags(account, pageNumber, pageSize, keyword);
    }

    @GetMapping("non-users/{account}")
    public List<Tag> allTagsWithoutUsers(
            @PathVariable("account") String account,
            @RequestParam(name = "pn", defaultValue = "0") int pageNumber,
            @RequestParam(name = "ps", defaultValue = "100") int pageSize,
            @RequestParam(name = "kw", required = false) String keyword
    ) {
        return this.weiXinService.findTags(account, pageNumber, pageSize, keyword)
                .getContent()
                .stream()
                .map(tag -> {
                    Tag t = new Tag();
                    t.setTagId(tag.getTagId());
                    t.setWeChatTagId(tag.getWeChatTagId());
                    t.setName(tag.getName());
                    return t;
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    public Result save(@RequestBody Tag tag) {
        return this.weiXinService.saveTag(tag);
    }
}
