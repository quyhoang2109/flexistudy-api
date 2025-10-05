package com.quyhoang.flexistudy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyCreationRequest {
    @NotBlank
    String name;
    String description;
    String logoUrl;
    String website;
    Integer memberNumber;
}
