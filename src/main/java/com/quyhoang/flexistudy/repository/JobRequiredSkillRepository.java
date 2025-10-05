package com.quyhoang.flexistudy.repository;

import com.quyhoang.flexistudy.entity.JobRequiredSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRequiredSkillRepository extends JpaRepository<JobRequiredSkill, String> {
    List<JobRequiredSkill> findByJob_Id(String jobId);

    Optional<JobRequiredSkill> findByJob_IdAndSkill_Id(String jobId, String skillId);

    void deleteByJob_IdAndSkill_Id(String jobId, String skillId);

    boolean existsByJob_IdAndSkill_Id(String jobId, String skillId);
}
