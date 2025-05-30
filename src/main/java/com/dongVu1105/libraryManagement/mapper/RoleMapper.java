package com.dongVu1105.libraryManagement.mapper;

import com.dongVu1105.libraryManagement.dto.request.RoleRequest;
import com.dongVu1105.libraryManagement.dto.response.RoleResponse;
import com.dongVu1105.libraryManagement.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    public Role toRole (RoleRequest request);
    public RoleResponse toRoleResponse (Role role);
}
