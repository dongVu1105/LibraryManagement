package com.dongVu1105.libraryManagement.mapper;

import com.dongVu1105.libraryManagement.dto.request.AccountCreationByAdminRequest;
import com.dongVu1105.libraryManagement.dto.request.UserCreationRequest;
import com.dongVu1105.libraryManagement.dto.request.UserUpdateRequest;
import com.dongVu1105.libraryManagement.dto.response.UserResponse;
import com.dongVu1105.libraryManagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    public User toUser (UserCreationRequest userCreationRequest);
    @Mapping(target = "roles", ignore = true)
    public User toUser (AccountCreationByAdminRequest request);
    public UserResponse toUserResponse (User user);
    @Mapping(target = "roles", ignore = true)
    public void updateUser (UserUpdateRequest request,@MappingTarget User user);
}
