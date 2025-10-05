package com.quyhoang.flexistudy.mapper;

import com.quyhoang.flexistudy.dto.request.RoleRequest;
import com.quyhoang.flexistudy.dto.response.RoleResponse;
import com.quyhoang.flexistudy.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
