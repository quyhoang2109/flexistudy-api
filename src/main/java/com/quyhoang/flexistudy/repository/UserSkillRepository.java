package com.quyhoang.flexistudy.repository;

import com.quyhoang.flexistudy.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, String> {
    List<UserSkill> findByUser_Id(String userId);

    Optional<UserSkill> findByUser_IdAndSkill_Id(String userId, String skillId);

    boolean existsByUser_IdAndSkill_Id(String userId, String skillId);
}
