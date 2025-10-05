package com.quyhoang.flexistudy.controller;

import com.quyhoang.flexistudy.dto.request.ApiResponse;
import com.quyhoang.flexistudy.dto.request.JobRequiredSkillRequest;
import com.quyhoang.flexistudy.dto.response.JobRequiredSkillResponse;
import com.quyhoang.flexistudy.service.JobRequiredSkillService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs/required-skills/{jobId}")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JobRequiredSkillController {
    JobRequiredSkillService service;

    @GetMapping
    ApiResponse<List<JobRequiredSkillResponse>> list(@PathVariable String jobId) {
        return ApiResponse.<List<JobRequiredSkillResponse>>builder()
                .result(service.listByJob(jobId))
                .build();
    }

    // Bulk replace toàn bộ danh sách
    @PutMapping
    ApiResponse<List<JobRequiredSkillResponse>> replaceAll(
            @PathVariable String jobId,
            @RequestBody List<@Valid JobRequiredSkillRequest> reqs) {
        return ApiResponse.<List<JobRequiredSkillResponse>>builder()
                .result(service.replaceAll(jobId, reqs))
                .build();
    }

    // Thêm 1 skill
    @PostMapping
    ApiResponse<JobRequiredSkillResponse> addOne(
            @PathVariable String jobId,
            @RequestBody @Valid JobRequiredSkillRequest req) {
        return ApiResponse.<JobRequiredSkillResponse>builder()
                .result(service.addOne(jobId, req))
                .build();
    }

    // Cập nhật 1 skill (theo skillId hiện tại trong path)
    @PatchMapping("/{skillId}")
    ApiResponse<JobRequiredSkillResponse> updateOne(
            @PathVariable String jobId,
            @PathVariable String skillId,
            @RequestBody @Valid JobRequiredSkillRequest req) {
        return ApiResponse.<JobRequiredSkillResponse>builder()
                .result(service.updateOne(jobId, skillId, req))
                .build();
    }

    // Xoá 1 skill
    @DeleteMapping("/{skillId}")
    ApiResponse<String> deleteOne(@PathVariable String jobId, @PathVariable String skillId) {
        service.deleteOne(jobId, skillId);
        return ApiResponse.<String>builder().result("Deleted").build();
    }
}
