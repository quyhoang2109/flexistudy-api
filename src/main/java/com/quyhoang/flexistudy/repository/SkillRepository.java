package com.quyhoang.flexistudy.repository;

import com.quyhoang.flexistudy.entity.Skill;
import com.quyhoang.flexistudy.enums.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill,String> {
    boolean existsByNameIgnoreCase(String name);

    List<Skill> findAllByCategory(SkillCategory category);
}
