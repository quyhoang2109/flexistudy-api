package com.quyhoang.identity_service.mapper;

import com.quyhoang.identity_service.dto.request.PermissionRequest;
import com.quyhoang.identity_service.dto.response.PermissionResponse;
import com.quyhoang.identity_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
