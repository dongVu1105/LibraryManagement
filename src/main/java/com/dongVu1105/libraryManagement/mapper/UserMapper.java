package com.dongVu1105.libraryManagement.mapper;

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
    public User toUser (UserCreationRequest userCreationRequest);
    public UserResponse toUserResponse (User user);
    public void updateUser (UserUpdateRequest request,@MappingTarget User user);
}
