package com.dongVu1105.libraryManagement.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    boolean gender;
    String password;
    String phoneNumber;
    LocalDate birthday;
    String avatar;
    Set<RoleResponse> roles;
}
