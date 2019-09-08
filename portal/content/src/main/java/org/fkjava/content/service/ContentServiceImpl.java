package org.fkjava.content.service;

import org.fkjava.commons.domain.Result;
import org.fkjava.content.domain.Article;
import org.fkjava.content.domain.Tag;
import org.fkjava.content.repository.ArticleRepository;
import org.fkjava.content.repository.TagRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ContentServiceImpl implements ContentService {

    // 注入DAO
    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;

    public ContentServiceImpl(TagRepository tagRepository, ArticleRepository articleRepository) {
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Tag> findAllTags() {
        // 排序规则
        Sort sort = Sort.by(Sort.Order.asc("name"));
        // 查询所有数据
        return tagRepository.findAll(sort);
    }

    @Override
    public Page<Article> findArticles(Integer pageNumber, Integer pageSize, String keyword, List<String> tagsId) {
        // 如果有tagsId，意味着要通过标签来搜索
        // 如果有关键字，就需要根据关键字搜索

        // 分页条件
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Article> page;
        if (StringUtils.isEmpty(keyword) && (tagsId == null || tagsId.isEmpty())) {
            page = this.articleRepository.findAll(pageable);
        } else {
            Specification<Article> spec = (root, query, builder) -> {

                // 去除重复的记录
                query.distinct(true);

                // 其实要求返回一个谓词用于条件搜索

                List<Predicate> predicates = new LinkedList<>();

                if (!StringUtils.isEmpty(keyword)) {
                    // title like ?
                    Predicate titleLike = builder.like(root.get("title"), "%" + keyword + "%");
                    predicates.add(titleLike);
                }

                if (tagsId != null && !tagsId.isEmpty()) {
                    // from Article a join a.tags in (...)
//                    List<Tag> tags = this.tagRepository.findAllById(tagsId);
//                    Predicate tagsIn = root.join("tags").in(tags);
//                    predicates.add(tagsIn);

                    // 这种方式不需要先查询一次，直接把ID传递进去
                    predicates.add(root.join("tags").get("id").in(tagsId));
                }

                if (predicates.size() == 1) {
                    return predicates.get(0);
                } else {
                    // 把所有条件转换为数组，并且构建成and条件返回
                    // title like ? and tags in (...)
                    return builder.and(predicates.toArray(new Predicate[0]));
                }
            };
            page = this.articleRepository.findAll(spec, pageable);
        }

        // 当某些字段不需要返回到页面的时候，往往就是新建一个Page对象，然后把需要的属性自己一个个的放入新对象里面
        List<Article> newContent = page.getContent().stream()
                .map(article -> new Article(
                        article.getId(),
                        article.getTitle(),
                        article.getSummary(),
                        null,
                        null,
                        null,
                        article.getEffectTime(),
                        article.getPublishTime(),
                        article.isDeleted()
                ))
                .collect(Collectors.toList());

        return (Page<Article>) new PageImpl(newContent, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public Result saveArticle(Article article) {
        // 整理标签的名称，如果标签名称存在于数据库里面则关联起来使用；否则查询新的标签
        // 1.根据标签名字查询所有符合条件的标签
        // 假设数据库里面有 通知 内部 等两个标签，新增文章的时候标签的名称是 通知 内容 放假
        // 此时 放假 这个标签在数据库找不到，得到的tags里面不包含【放假】
        List<String> tagNames = article.getTagNames();
        List<Tag> tags = this.tagRepository.findByNameIn(tagNames);

        // HashSet底层就是HashMap，只是底层存储的时候value永远是固定的
        Set<String> existsNames = tags.stream()
                .map(Tag::getName)// 只要标签的名称
                .collect(Collectors.toSet());

        Set<String> newTagNames = tagNames.stream()
                // 找到不存在的新标签名称
                .filter(name -> !existsNames.contains(name))
                .collect(Collectors.toSet());

        List<Tag> allTags = new LinkedList<>(tags);

        // 把新的标签存储到数据库
        newTagNames.forEach(name -> {
            Tag newTag = new Tag();
            newTag.setName(name);
            this.tagRepository.save(newTag);
            allTags.add(newTag);
        });

        article.setTags(allTags);

        this.articleRepository.save(article);

        return Result.ok("文章保存成功");
    }

    @Override
    public Article findArticleById(String id) {
        return this.articleRepository.findById(id).orElse(null);
    }
}
