package org.fkjava.user.service;

import org.fkjava.commons.domain.Result;
import org.fkjava.user.domain.Role;

import java.util.List;

public interface RoleService {

    Role findByRoleKey(String roleKey);

    Result save(Role role);

    Result delete(String id);

    Result deleteByRoleKey(String roleKey);

    List<Role> findByTenantIdAndBuiltInIsTrue(String tenantId);

    List<Role> findByTenantIdAndFixedIsTrue(String tenantId);
}
