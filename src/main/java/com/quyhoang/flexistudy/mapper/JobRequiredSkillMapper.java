package com.quyhoang.flexistudy.mapper;

import com.quyhoang.flexistudy.dto.response.JobRequiredSkillResponse;
import com.quyhoang.flexistudy.entity.JobRequiredSkill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobRequiredSkillMapper {
    @Mapping(target = "skillId", source = "skill.id")
    @Mapping(target = "skillName", source = "skill.name")
    JobRequiredSkillResponse toResponse(JobRequiredSkill entity);
}
