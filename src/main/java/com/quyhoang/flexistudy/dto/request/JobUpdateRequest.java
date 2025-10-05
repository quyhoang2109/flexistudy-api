package com.quyhoang.flexistudy.dto.request;

import com.quyhoang.flexistudy.enums.EmployeeType;
import com.quyhoang.flexistudy.enums.JobCategory;
import com.quyhoang.flexistudy.enums.JobStatus;
import com.quyhoang.flexistudy.enums.WorkMode;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobUpdateRequest {
    String title;
    String description;
    EmployeeType type;
    Integer minSalary;
    JobCategory category;
    Integer maxSalary;
    String currency;
    WorkMode mode;
    String city;
    JobStatus status;
    Set<String> skillIds;
}
