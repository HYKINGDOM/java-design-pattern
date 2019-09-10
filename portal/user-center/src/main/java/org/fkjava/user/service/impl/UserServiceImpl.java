package org.fkjava.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fkjava.commons.domain.Result;
import org.fkjava.user.domain.Role;
import org.fkjava.user.domain.User;
import org.fkjava.user.repository.UserRepository;
import org.fkjava.user.service.RoleService;
import org.fkjava.user.service.UserService;
import org.fkjava.verify.api.VerifyCodeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    // 远程服务，直接注入进来就可以使用了！
    private final VerifyCodeService verifyCodeService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserServiceImpl(
            UserRepository userRepository,
            VerifyCodeService verifyCodeService,
            PasswordEncoder passwordEncoder,
            RoleService roleService) {
        this.userRepository = userRepository;
        this.verifyCodeService = verifyCodeService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public Result registry(User user, String code) {
        // 检查验证码是否有效
        Result verifyResult = verifyCodeService.verify(user.getPhone(), code);
        if (verifyResult.getCode() != 1) {
            return verifyResult;
        }
        // 检查登录名是否被占用
        User old = this.userRepository.findByLoginName(user.getLoginName());
        if (old != null) {
            return Result.error("登录名已经被占用");
        }
        // 对密码进行加密
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);

        this.fillUserProperties(user);

        this.userRepository.save(user);
        return Result.ok("注册成功");
    }

    @Override
    @Transactional
    public User loadByLoginName(String username) {
        User user = this.userRepository.findByLoginName(username);
        fillRoles(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User loadById(String id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            fillRoles(user);
            return user;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public User loadByOpenId(String openId) {
        User user = this.userRepository.findByOpenId(openId);
        fillRoles(user);
        return user;
    }

    @Override
    @Transactional
    public User loadByPhone(String phone) {
        User user = this.userRepository.findByPhone(phone);
        fillRoles(user);
        return user;
    }

    @Override
    @Transactional
    public User save(User user) {
        // 1.检查登录名或者OpenID是否重复
        User old = this.userRepository.findByOpenId(user.getOpenId());
        if (old == null) {
            old = this.userRepository.findByLoginName(user.getLoginName());
        }
        if (old != null) {
            // 存在旧的数据，修改！
            old.setEnabled(true);
            old.setName(user.getName());
            old.setOpenId(user.getOpenId());
            if (StringUtils.isEmpty(old.getLoginName())) {
                old.setLoginName(user.getLoginName());
            }
            user = old;
        } else {
            // 2.生成一个随机密码
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            // 3.设置用户属性
            user.setRegisteredTime(new Date());
            user.setEnabled(true);
            user.setAccountNonLocked(true);
        }

        this.fillUserProperties(user);

        return this.userRepository.save(user);
    }

    private void fillRoles(User user) {
        if (user == null) {
            return;
        }
        if (user.getRoles() == null) {
            user.setRoles(new LinkedList<>());
        }
        user.getRoles().stream()
                .filter(Role::isFixed)
                .findFirst()
                .ifPresentOrElse(
                        role -> {
                        },
                        () -> {
                            String tenantId = user.getTenantId();
                            List<Role> fixedRoles = this.roleService
                                    .findByTenantIdAndFixedIsTrue(tenantId);
                            user.getRoles().addAll(fixedRoles);
                        });
    }

    private void fillUserProperties(User user) {
        if (user == null) {
            return;
        }
        // 检查用户是否包含默认角色，如果没有则把所有的默认角色都加上
        String tenantId = user.getTenantId();
        List<Role> fixedRoles = this.roleService
                .findByTenantIdAndFixedIsTrue(tenantId);
        // 1.如果没有角色，则赋予一个默认的角色列表
        if (user.getRoles() == null) {
            user.setRoles(new LinkedList<>());
        }
        // 2.删除固定角色，然后重新加入
        // 2.1.把所有固定的角色的ID放入一个Set集合中
        Set<String> fixedRoleIds = fixedRoles
                .stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
        // 2.2.根据上面的Set集合中的id，找到需要在User中删除的角色列表
        List<Role> removed = user.getRoles()
                .stream()
                .filter(role -> fixedRoleIds.contains(role.getId()))
                .collect(Collectors.toList());
        // 2.3.执行删除
        user.getRoles().removeAll(removed);
        // 加入固定角色
        user.getRoles().addAll(fixedRoles);

        user.setEnabled(true);
        user.setAccountNonLocked(true);
        // 账户180天内有效
        final long accountExpireTime = 6 * 30 * 24 * 60 * 60 * 1000L;
        user.setAccountExpireTime(
                new Date(System.currentTimeMillis() + accountExpireTime)
        );
        // 密码90天内有效
        final long passwordExpireTime = 3 * 30 * 24 * 60 * 60 * 1000L;
        user.setPasswordExpireTime(
                new Date(System.currentTimeMillis() + passwordExpireTime)
        );
    }

    @Override
    @Transactional
    public Result updateLoginName(String id, String loginName) {
        this.userRepository.findById(id).ifPresent(user -> user.setLoginName(loginName));
        return Result.ok("用户登录名修改成功");
    }

    @Override
    @Transactional
    public Result updatePhone(String id, String phone, String verifyCode) {
        Result result = this.verifyCodeService.verify(phone, verifyCode);
        if (result.getCode() == 1) {
            this.userRepository.findById(id).ifPresent(user -> user.setPhone(phone));
            return Result.ok("手机号码修改成功");
        } else {
            return result;
        }
    }

    @Override
    @Transactional
    public Result updateAccountNonLocked(String id, boolean accountNonLocked) {
        this.userRepository.findById(id).ifPresent(user -> user.setAccountNonLocked(accountNonLocked));
        return Result.ok("账户锁定状态修改成功");
    }

    @Override
    @Transactional
    public Result updateEnabled(String id, boolean enabled) {
        this.userRepository.findById(id).ifPresent(user -> user.setEnabled(enabled));
        return Result.ok("用户禁用状态修改成功");
    }

    @Override
    @Transactional
    public Result updatePassword(String id, String password) {
        this.userRepository.findById(id).ifPresent(user -> user.setPassword(this.passwordEncoder.encode(password)));
        return Result.ok("用户登录密码修改成功");
    }
}
