package com.quyhoang.flexistudy.mapper;

import com.quyhoang.flexistudy.dto.request.CompanyCreationRequest;
import com.quyhoang.flexistudy.dto.request.CompanyUpdateRequest;
import com.quyhoang.flexistudy.dto.response.CompanyResponse;
import com.quyhoang.flexistudy.entity.Company;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    @Mapping(target = "createdAt", ignore = true)
    Company toCompany(CompanyCreationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCompany(@MappingTarget Company company, CompanyUpdateRequest request);

    CompanyResponse toCompanyResponse(Company company);
}
