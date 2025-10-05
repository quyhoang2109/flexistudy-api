package com.quyhoang.flexistudy.mapper;

import com.quyhoang.flexistudy.dto.request.SkillRequest;
import com.quyhoang.flexistudy.dto.response.SkillResponse;
import com.quyhoang.flexistudy.entity.Skill;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    Skill toSkill(SkillRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSkill(@MappingTarget Skill skill, SkillRequest request);

    SkillResponse toSkillResponse(Skill skill);
}
