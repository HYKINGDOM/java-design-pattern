package org.fkjava.content.service;

import org.fkjava.commons.domain.Result;
import org.fkjava.content.domain.Article;
import org.fkjava.content.domain.Tag;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContentService {
    List<Tag> findAllTags();

    Page<Article> findArticles(Integer pageNumber, Integer pageSize, String keyword, List<String> tagsId);

    Result saveArticle(Article article);

    Article findArticleById(String id);
}
