package com.quyhoang.flexistudy.repository;

import com.quyhoang.flexistudy.entity.Role;
import com.quyhoang.flexistudy.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, RoleName> {
}
