package com.quyhoang.flexistudy.service;

import com.quyhoang.flexistudy.dto.request.SkillRequest;
import com.quyhoang.flexistudy.dto.response.SkillResponse;
import com.quyhoang.flexistudy.entity.Skill;
import com.quyhoang.flexistudy.enums.SkillCategory;
import com.quyhoang.flexistudy.exception.AppException;
import com.quyhoang.flexistudy.exception.ErrorCode;
import com.quyhoang.flexistudy.mapper.SkillMapper;
import com.quyhoang.flexistudy.repository.SkillRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SkillService {
    SkillRepository skillRepository;
    SkillMapper skillMapper;  // 👈 Thêm mapper vào

    @Transactional
    public SkillResponse create(SkillRequest request) {
        if (skillRepository.existsByNameIgnoreCase(request.getName())) {
            throw new AppException(ErrorCode.SKILL_EXISTED);
        }

        Skill skill = skillMapper.toSkill(request);
        return skillMapper.toSkillResponse(skillRepository.save(skill));
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getAll() {
        return skillRepository.findAll().stream()
                .map(skillMapper::toSkillResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SkillResponse getById(String id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SKILL_NOT_FOUND));
        return skillMapper.toSkillResponse(skill);
    }

    @Transactional
    public SkillResponse update(String id, SkillRequest request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SKILL_NOT_FOUND));

        skillMapper.updateSkill(skill, request);
        return skillMapper.toSkillResponse(skillRepository.save(skill));
    }

    @Transactional
    public void delete(String id) {
        if (!skillRepository.existsById(id)) {
            throw new AppException(ErrorCode.SKILL_NOT_FOUND);
        }
        skillRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> findByCategory(SkillCategory category) {
        return skillRepository.findAllByCategory(category).stream()
                .map(skillMapper::toSkillResponse)
                .toList();
    }
}
