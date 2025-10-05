package com.quyhoang.flexistudy.dto.response;

import com.quyhoang.flexistudy.enums.SkillCategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillResponse {
    String id;
    String name;
    String description;
    SkillCategory category;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
