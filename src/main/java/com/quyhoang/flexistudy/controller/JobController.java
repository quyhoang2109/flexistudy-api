package com.quyhoang.flexistudy.controller;

import com.quyhoang.flexistudy.dto.PageResponse;
import com.quyhoang.flexistudy.dto.request.ApiResponse;
import com.quyhoang.flexistudy.dto.request.JobCreationRequest;
import com.quyhoang.flexistudy.dto.request.JobUpdateRequest;
import com.quyhoang.flexistudy.dto.response.JobResponse;
import com.quyhoang.flexistudy.service.JobService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JobController {
    JobService jobService;

    @PostMapping
    ApiResponse<JobResponse> createJob(@RequestBody  JobCreationRequest request) {
        System.out.println("📨 Received createJob request");
        return ApiResponse.<JobResponse>builder()
                .result(jobService.createJob(request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<JobResponse>> getAllJobs(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String search
    ) {
        PageResponse<JobResponse> response = jobService.getAllJobs(page, size, search);
        return ApiResponse.<PageResponse<JobResponse>>builder()
                .result(response)
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<JobResponse> getJobById(@PathVariable String id) {
        return ApiResponse.<JobResponse>builder()
                .result(jobService.getJobById(id))
                .build();
    }

    @PutMapping("/{id}") // hoặc @PatchMapping nếu bạn muốn semantics partial update
    ApiResponse<JobResponse> updateJob(@PathVariable String id,
                                       @RequestBody @Valid JobUpdateRequest request) {
        return ApiResponse.<JobResponse>builder()
                .result(jobService.updateJob(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteJob(@PathVariable String id) {
        jobService.deleteJob(id);
        return ApiResponse.<String>builder()
                .result("Job has been deleted")
                .build();
    }
}
