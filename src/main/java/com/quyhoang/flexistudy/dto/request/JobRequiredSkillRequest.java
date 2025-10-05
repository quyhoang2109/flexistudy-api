package com.quyhoang.flexistudy.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobRequiredSkillRequest {
    @NotBlank
    String skillId;

    @Min(1) @Max(5) // tuỳ range bạn muốn
    int minLevel;

    @DecimalMin("0.0") @DecimalMax("1.0")
    @Digits(integer = 1, fraction = 3)
    BigDecimal weight; // sẽ parse về BigDecimal trong mapper/service
}
