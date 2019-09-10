package org.fkjava.wechat.repository.jpa;

import org.fkjava.wechat.domain.Tag;
import org.fkjava.wechat.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, String>, JpaSpecificationExecutor<Tag> {
    List<Tag> findByAccountAndWeChatTagIdIn(String account, List<Integer> tagIdList);

    List<Tag> findByUserInfosIn(List<UserInfo> userInfo);
}
