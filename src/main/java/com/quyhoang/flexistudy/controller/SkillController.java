package com.quyhoang.flexistudy.controller;

import com.quyhoang.flexistudy.dto.request.ApiResponse;
import com.quyhoang.flexistudy.dto.request.SkillRequest;
import com.quyhoang.flexistudy.dto.response.SkillResponse;
import com.quyhoang.flexistudy.enums.SkillCategory;
import com.quyhoang.flexistudy.service.SkillService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SkillController {
    SkillService skillService;

    @PostMapping
    ApiResponse<SkillResponse> create(@RequestBody @Valid SkillRequest request) {
        return ApiResponse.<SkillResponse>builder()
                .result(skillService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<SkillResponse>> getAll() {
        return ApiResponse.<List<SkillResponse>>builder()
                .result(skillService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<SkillResponse> getById(@PathVariable String id) {
        return ApiResponse.<SkillResponse>builder()
                .result(skillService.getById(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<SkillResponse> update(@PathVariable String id, @RequestBody @Valid SkillRequest request) {
        return ApiResponse.<SkillResponse>builder()
                .result(skillService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> delete(@PathVariable String id) {
        skillService.delete(id);
        return ApiResponse.<String>builder()
                .result("Skill has been deleted")
                .build();
    }

    @GetMapping("/category/{category}")
    ApiResponse<List<SkillResponse>> findByCategory(@PathVariable SkillCategory category) {
        return ApiResponse.<List<SkillResponse>>builder()
                .result(skillService.findByCategory(category))
                .build();
    }
}
