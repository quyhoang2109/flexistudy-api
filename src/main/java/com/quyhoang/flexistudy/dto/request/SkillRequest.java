package com.quyhoang.flexistudy.dto.request;

import com.quyhoang.flexistudy.enums.SkillCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillRequest {
    @NotBlank
    String name;

    String description;

    @NotNull
    SkillCategory category;
}
