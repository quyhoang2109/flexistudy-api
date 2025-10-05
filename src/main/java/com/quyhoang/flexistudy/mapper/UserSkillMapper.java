package com.quyhoang.flexistudy.mapper;

import com.quyhoang.flexistudy.dto.request.UserSkillRequest;
import com.quyhoang.flexistudy.dto.response.UserSkillResponse;
import com.quyhoang.flexistudy.entity.UserSkill;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserSkillMapper {
    UserSkill toEntity(UserSkillRequest req);

    @Mapping(target = "skillId", source = "skill.id")
    @Mapping(target = "skillName", source = "skill.name")
    UserSkillResponse toResponse(UserSkill entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget UserSkill entity, UserSkillRequest req);
}
