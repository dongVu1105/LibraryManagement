package com.dongVu1105.libraryManagement.mapper;

import com.dongVu1105.libraryManagement.dto.request.CategoryRequest;
import com.dongVu1105.libraryManagement.dto.response.CategoryResponse;
import com.dongVu1105.libraryManagement.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory (CategoryRequest request);
    CategoryResponse toCategoryResponse (Category category);
    void updateCategory (CategoryRequest request, @MappingTarget Category category);
}
