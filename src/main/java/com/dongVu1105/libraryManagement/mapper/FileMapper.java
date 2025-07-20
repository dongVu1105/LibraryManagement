package com.dongVu1105.libraryManagement.mapper;

import com.dongVu1105.libraryManagement.dto.request.FileInfo;
import com.dongVu1105.libraryManagement.entity.FileManagement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {
    @Mapping(source = "name", target = "id")
    FileManagement toFileManagement (FileInfo fileInfo);
}
