package com.dongVu1105.libraryManagement.dto.request;

import com.dongVu1105.libraryManagement.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String username;
    String firstName;
    String lastName;
    boolean gender;
    String password;
    String phoneNumber;
    LocalDate birthday;
    Set<String> roles;
}
