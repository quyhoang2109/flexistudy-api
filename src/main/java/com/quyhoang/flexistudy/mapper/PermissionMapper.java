package com.quyhoang.flexistudy.mapper;

import com.quyhoang.flexistudy.dto.request.PermissionRequest;
import com.quyhoang.flexistudy.dto.response.PermissionResponse;
import com.quyhoang.flexistudy.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
