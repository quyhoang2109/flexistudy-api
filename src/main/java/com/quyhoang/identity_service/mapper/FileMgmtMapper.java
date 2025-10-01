package com.quyhoang.identity_service.mapper;

import com.quyhoang.identity_service.dto.FileInfo;
import com.quyhoang.identity_service.entity.FileMgmt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMgmtMapper {
    @Mapping(target = "id", source = "name")
    FileMgmt toFileMgmt(FileInfo fileInfo);
}