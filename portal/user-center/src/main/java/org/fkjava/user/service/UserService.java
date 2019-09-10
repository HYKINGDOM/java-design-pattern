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

    Result updatePhone(String id, String phone, String verifyCode);

    Result updateLoginName(String id, String loginName);

    Result updateAccountNonLocked(String id, boolean accountNonLocked);

    Result updateEnabled(String id, boolean enabled);

    Result updatePassword(String id, String password);
}
