package com.dongVu1105.libraryManagement.dto.request;

import jakarta.validation.constraints.Size;
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
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;
    String phoneNumber;
    LocalDate birthday;
}
