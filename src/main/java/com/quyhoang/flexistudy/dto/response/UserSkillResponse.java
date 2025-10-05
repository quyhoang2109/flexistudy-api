package com.quyhoang.flexistudy.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSkillResponse {
    String id;
    String skillId;
    String skillName;
    BigDecimal level;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
