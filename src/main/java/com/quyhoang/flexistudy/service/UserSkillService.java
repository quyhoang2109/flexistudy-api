package com.quyhoang.flexistudy.service;

import com.quyhoang.flexistudy.dto.request.UserSkillRequest;
import com.quyhoang.flexistudy.dto.response.UserSkillResponse;
import com.quyhoang.flexistudy.entity.Skill;
import com.quyhoang.flexistudy.entity.User;
import com.quyhoang.flexistudy.entity.UserSkill;
import com.quyhoang.flexistudy.exception.AppException;
import com.quyhoang.flexistudy.exception.ErrorCode;
import com.quyhoang.flexistudy.mapper.UserSkillMapper;
import com.quyhoang.flexistudy.repository.SkillRepository;
import com.quyhoang.flexistudy.repository.UserRepository;
import com.quyhoang.flexistudy.repository.UserSkillRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserSkillService {
    UserSkillRepository userSkillRepository;
    UserRepository userRepository;
    SkillRepository skillRepository;
    UserSkillMapper userSkillMapper;

    @Transactional
    public UserSkillResponse addUserSkill(String userId, UserSkillRequest req) {
        if (userSkillRepository.existsByUser_IdAndSkill_Id(userId, req.getSkillId())) {
            throw new AppException(ErrorCode.SKILL_EXISTED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.SKILL_NOT_FOUND));

        Skill skill = skillRepository.findById(req.getSkillId())
                .orElseThrow(() -> new AppException(ErrorCode.SKILL_NOT_FOUND));

        UserSkill userSkill = UserSkill.builder()
                .user(user)
                .skill(skill)
                .level(req.getLevel())
                .build();

        return userSkillMapper.toResponse(userSkillRepository.save(userSkill));
    }

    @Transactional
    public UserSkillResponse updateUserSkill(String userId, String skillId, UserSkillRequest req) {
        UserSkill userSkill = userSkillRepository.findByUser_IdAndSkill_Id(userId, skillId)
                .orElseThrow(() -> new AppException(ErrorCode.SKILL_NOT_FOUND));

        userSkill.setLevel(req.getLevel());

        return userSkillMapper.toResponse(userSkillRepository.save(userSkill));
    }

    @Transactional
    public void deleteUserSkill(String userId, String skillId) {
        UserSkill userSkill = userSkillRepository.findByUser_IdAndSkill_Id(userId, skillId)
                .orElseThrow(() -> new AppException(ErrorCode.SKILL_NOT_FOUND));
        userSkillRepository.delete(userSkill);
    }

    @Transactional
    public List<UserSkillResponse> getUserSkills(String userId) {
        return userSkillRepository.findByUser_Id(userId)
                .stream()
                .map(userSkillMapper::toResponse)
                .toList();
    }
}
