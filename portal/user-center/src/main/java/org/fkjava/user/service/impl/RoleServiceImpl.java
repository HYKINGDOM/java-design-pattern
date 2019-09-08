package org.fkjava.user.service.impl;

import org.fkjava.commons.domain.Result;
import org.fkjava.user.domain.Role;
import org.fkjava.user.repository.RoleRepository;
import org.fkjava.user.service.RoleService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl
        implements RoleService, ApplicationContextAware {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByRoleKey(String roleKey) {
        return null;
    }

    @Override
    public Result save(Role role) {
        return null;
    }

    @Override
    public Result delete(String id) {
        return null;
    }

    @Override
    public Result deleteByRoleKey(String roleKey) {
        return null;
    }

    @Override
    public List<Role> findByTenantIdAndBuiltInIsTrue(String tenantId) {
        return this.roleRepository.findByTenantIdAndBuiltInIsTrue(tenantId);
    }

    @Override
    public List<Role> findByTenantIdAndFixedIsTrue(String tenantId) {
        return this.roleRepository.findByTenantIdAndFixedIsTrue(tenantId);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        List<Role> all = this.roleRepository.findAll();
        all.stream()
                .filter(role -> role.getRoleKey().equals("USER"))
                .findFirst()
                .ifPresentOrElse(role -> {
                }, () -> {
                    // 不存在的时候，新增一个！
                    Role role = new Role();
                    role.setName("普通用户");
                    role.setRoleKey("USER");
                    role.setFixed(true);
                    role.setBuiltIn(true);
                    this.roleRepository.save(role);
                });
        all.stream()
                .filter(role -> role.getRoleKey().equals("ADMIN"))
                .findFirst()
                .ifPresentOrElse(role -> {
                }, () -> {
                    // 不存在的时候，新增一个！
                    Role role = new Role();
                    role.setName("超级管理员");
                    role.setRoleKey("ADMIN");
                    role.setFixed(false);
                    role.setBuiltIn(true);
                    this.roleRepository.save(role);
                });
    }
}
