package com.quyhoang.flexistudy.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyUpdateRequest {
    String description;
    String logoUrl;
    String website;
    Integer memberNumber;
}
