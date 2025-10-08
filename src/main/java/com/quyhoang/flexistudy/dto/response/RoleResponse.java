package com.quyhoang.flexistudy.dto.response;

import com.quyhoang.flexistudy.enums.RoleName;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    private RoleName name;
    String description;
    Set<PermissionResponse> permissions;
}
