package com.dongVu1105.libraryManagement.dto.request;

import com.dongVu1105.libraryManagement.validation.BirthDayConstraint;
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
public class AccountCreationByAdminRequest {
    @Size(min = 5, message = "INVALID_USERNAME")
    String username;
    String firstName;
    String lastName;
    boolean gender;
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;
    String phoneNumber;
    @BirthDayConstraint(min = 16, message = "INVALID_BIRTH")
    LocalDate birthday;
    Set<String> roles;
}
