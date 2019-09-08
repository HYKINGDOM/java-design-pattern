package org.fkjava.wechat.repository.jpa;

import org.fkjava.wechat.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, String>, JpaSpecificationExecutor<Tag> {
}
