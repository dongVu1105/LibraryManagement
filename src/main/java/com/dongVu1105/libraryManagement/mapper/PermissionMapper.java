package com.dongVu1105.libraryManagement.mapper;

import com.dongVu1105.libraryManagement.dto.request.PermissionRequest;
import com.dongVu1105.libraryManagement.dto.request.RoleRequest;
import com.dongVu1105.libraryManagement.dto.response.PermissionResponse;
import com.dongVu1105.libraryManagement.dto.response.RoleResponse;
import com.dongVu1105.libraryManagement.entity.Permission;
import com.dongVu1105.libraryManagement.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    public Permission toPermission (PermissionRequest request);
    public PermissionResponse toPermissionResponse (Permission permission);
}
