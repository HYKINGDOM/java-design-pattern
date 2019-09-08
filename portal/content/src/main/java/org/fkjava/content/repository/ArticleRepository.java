package org.fkjava.content.repository;

import org.fkjava.content.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
// JpaSpecificationExecutor可以使用复杂的自定义查询
public interface ArticleRepository extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {
}
