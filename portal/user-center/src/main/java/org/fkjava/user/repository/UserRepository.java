package org.fkjava.user.repository;

import org.fkjava.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByLoginName(String loginName);

    User findByOpenId(String openId);

    User findByPhone(String phone);
}
