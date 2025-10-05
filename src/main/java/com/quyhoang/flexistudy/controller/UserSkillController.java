package com.quyhoang.flexistudy.controller;

import com.quyhoang.flexistudy.dto.request.ApiResponse;
import com.quyhoang.flexistudy.dto.request.UserSkillRequest;
import com.quyhoang.flexistudy.dto.response.UserSkillResponse;
import com.quyhoang.flexistudy.service.UserSkillService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/skills/{userId}")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserSkillController {
    UserSkillService userSkillService;

    @PostMapping
    public ApiResponse<UserSkillResponse> addUserSkill(@PathVariable String userId,
                                                       @RequestBody UserSkillRequest request) {
        return ApiResponse.<UserSkillResponse>builder()
                .result(userSkillService.addUserSkill(userId, request))
                .build();
    }

    @PutMapping("/{skillId}")
    public ApiResponse<UserSkillResponse> updateUserSkill(@PathVariable String userId,
                                                          @PathVariable String skillId,
                                                          @RequestBody UserSkillRequest request) {
        return ApiResponse.<UserSkillResponse>builder()
                .result(userSkillService.updateUserSkill(userId, skillId, request))
                .build();
    }

    @DeleteMapping("/{skillId}")
    public ApiResponse<Void> deleteUserSkill(@PathVariable String userId,
                                             @PathVariable String skillId) {
        userSkillService.deleteUserSkill(userId, skillId);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping
    public ApiResponse<List<UserSkillResponse>> getUserSkills(@PathVariable String userId) {
        return ApiResponse.<List<UserSkillResponse>>builder()
                .result(userSkillService.getUserSkills(userId))
                .build();
    }
}
