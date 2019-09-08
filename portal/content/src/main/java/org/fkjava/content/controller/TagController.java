package org.fkjava.content.controller;

import org.fkjava.content.domain.Tag;
import org.fkjava.content.service.ContentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/tag")
public class TagController {

    private final ContentService service;

    public TagController(ContentService service) {
        this.service = service;
    }

    // 如果希望返回的数据里面有分类，则需要创建两个类。分类通常就是品牌、型号、大小等待，一个分类下面有多个标签。
    // Catalog -> Tag
    // 如果不考虑分类，则直接建立一个Tag类即可。
    @GetMapping
    @ResponseBody// 直接返回对象，如果客户端要求JSON，会自动转换为JSON
    public List<Tag> getAllTags() {
        return service.findAllTags();
    }
}
