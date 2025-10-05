package com.quyhoang.flexistudy.service;

import com.quyhoang.flexistudy.dto.request.JobRequiredSkillRequest;
import com.quyhoang.flexistudy.dto.response.JobRequiredSkillResponse;
import com.quyhoang.flexistudy.entity.Job;
import com.quyhoang.flexistudy.entity.JobRequiredSkill;
import com.quyhoang.flexistudy.entity.Skill;
import com.quyhoang.flexistudy.exception.AppException;
import com.quyhoang.flexistudy.exception.ErrorCode;
import com.quyhoang.flexistudy.mapper.JobRequiredSkillMapper;
import com.quyhoang.flexistudy.repository.JobRepository;
import com.quyhoang.flexistudy.repository.JobRequiredSkillRepository;
import com.quyhoang.flexistudy.repository.SkillRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobRequiredSkillService {
    JobRepository jobRepository;
    SkillRepository skillRepository;
    JobRequiredSkillRepository jrsRepository;
    JobRequiredSkillMapper jrsMapper;

    @Transactional(readOnly = true)
    public List<JobRequiredSkillResponse> listByJob(String jobId) {
        ensureJobExists(jobId);
        return jrsRepository.findByJob_Id(jobId)
                .stream().map(jrsMapper::toResponse).toList();
    }

    @Transactional
    public List<JobRequiredSkillResponse> replaceAll(String jobId, List<JobRequiredSkillRequest> reqs) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_FOUND));

        // Xoá toàn bộ cũ
        jrsRepository.findByJob_Id(jobId).forEach(jrsRepository::delete);

        // Thêm mới theo danh sách
        var mapSkill = fetchSkillMap(reqs.stream().map(JobRequiredSkillRequest::getSkillId).toList());
        List<JobRequiredSkill> toSave = reqs.stream().map(r ->
                JobRequiredSkill.builder()
                        .job(job)
                        .skill(mapSkill.get(r.getSkillId()))
                        .minLevel(r.getMinLevel())
                        .weight(r.getWeight())
                        .build()
        ).toList();
        jrsRepository.saveAll(toSave);

        return toSave.stream().map(jrsMapper::toResponse).toList();
    }

    @Transactional
    public JobRequiredSkillResponse addOne(String jobId, JobRequiredSkillRequest r) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_FOUND));

        if (jrsRepository.existsByJob_IdAndSkill_Id(jobId, r.getSkillId())) {
            throw new AppException(ErrorCode.JOB_REQUIRED_EXISTED);
        }

        Skill skill = skillRepository.findById(r.getSkillId())
                .orElseThrow(() -> new AppException(ErrorCode.SKILL_NOT_FOUND));

        JobRequiredSkill saved = jrsRepository.save(
                JobRequiredSkill.builder()
                        .job(job)
                        .skill(skill)
                        .minLevel(r.getMinLevel())
                        .weight(r.getWeight())
                        .build()
        );
        return jrsMapper.toResponse(saved);
    }

    @Transactional
    public JobRequiredSkillResponse updateOne(String jobId, String skillId, JobRequiredSkillRequest r) {
        JobRequiredSkill jrs = jrsRepository.findByJob_IdAndSkill_Id(jobId, skillId)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_SKILL_REQUIRED_IS_NOT_FOUND)); // tạo ErrorCode phù hợp

        // nếu cho phép đổi skill, lấy skill mới
        if (r.getSkillId() != null && !r.getSkillId().equals(skillId)) {
            Skill newSkill = skillRepository.findById(r.getSkillId())
                    .orElseThrow(() -> new AppException(ErrorCode.SKILL_NOT_FOUND));
            jrs.setSkill(newSkill);
        }
        jrs.setMinLevel(r.getMinLevel());
        jrs.setWeight(r.getWeight());
        return jrsMapper.toResponse(jrsRepository.save(jrs));
    }

    @Transactional
    public void deleteOne(String jobId, String skillId) {
        ensureJobExists(jobId);
        jrsRepository.deleteByJob_IdAndSkill_Id(jobId, skillId);
    }

    // helpers
    private void ensureJobExists(String jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new AppException(ErrorCode.JOB_NOT_FOUND);
        }
    }

    private Map<String, Skill> fetchSkillMap(List<String> ids) {
        var list = skillRepository.findAllById(ids);
        if (list.size() != new HashSet<>(ids).size()) {
            throw new AppException(ErrorCode.SKILL_NOT_FOUND);
        }
        Map<String, Skill> map = new HashMap<>();
        for (Skill s : list) map.put(s.getId(), s);
        return map;
    }
}
