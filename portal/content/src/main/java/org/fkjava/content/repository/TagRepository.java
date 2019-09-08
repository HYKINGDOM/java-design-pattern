package org.fkjava.content.repository;

import org.fkjava.content.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

    // 此时会自动生成 name in (...) 查询
    List<Tag> findByNameIn(List<String> tagNames);
}
