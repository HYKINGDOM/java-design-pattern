package org.fkjava.user.repository;

import org.fkjava.user.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByName(String name);

    List<Role> findByTenantIdAndFixedIsTrue(String tenantId);

    List<Role> findByTenantIdAndBuiltInIsTrue(String tenantId);
}
