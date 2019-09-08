package org.fkjava.user.service;


import org.fkjava.commons.domain.Result;
import org.fkjava.user.domain.User;

public interface UserService {

    Result registry(User user, String code);

    User save(User user);

    User loadByLoginName(String loginName);

    User loadById(String id);

    User loadByOpenId(String openId);

    User loadByPhone(String phone);
}
