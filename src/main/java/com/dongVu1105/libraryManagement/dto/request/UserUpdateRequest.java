package com.dongVu1105.libraryManagement.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String firstName;
    String lastName;
    boolean gender;
    String password;
    String phoneNumber;
    LocalDate birthday;
}
