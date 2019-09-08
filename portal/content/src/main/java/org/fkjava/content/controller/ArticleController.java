package org.fkjava.content.controller;

import org.fkjava.commons.converter.StringToDateTimePropertyEditor;
import org.fkjava.commons.domain.Result;
import org.fkjava.content.domain.Article;
import org.fkjava.content.service.ContentService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ContentService contentService;

    public ArticleController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    public Page<Article> find(
            // 0是第一页
            @RequestParam(name = "pn", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "ps", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "kw", required = false) String keyword,
            @RequestParam(name = "tagsId", required = false) ArrayList<String> tagsId
    ) {
        return contentService.findArticles(pageNumber, pageSize, keyword, tagsId);
    }

    @PostMapping
    // @RequestBody的含义：把整个请求体的内容转换为Java对象
    // 由于前端发送的是JSON格式的内容，JSON正好可以转换为Java对象。
    public Result save(@RequestBody Article article) {
        return this.contentService.saveArticle(article);
    }

    @GetMapping("{id}")
    public Article findById(
            @PathVariable("id") String id
    ) {
        return contentService.findArticleById(id);
    }

    // 注册控制器全局的转换器，所有发送给当前控制器的数据，都会使用这里注册的转换器。
    @InitBinder
    public void bindDate(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new StringToDateTimePropertyEditor());
    }

    @PostMapping("{id}")
    public Result save(@PathVariable("id") String id, @RequestBody Article article) {
        article.setId(id);
        return this.contentService.saveArticle(article);
    }
}
