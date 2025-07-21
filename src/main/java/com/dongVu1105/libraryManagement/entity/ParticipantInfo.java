package com.dongVu1105.libraryManagement.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipantInfo {
    String username;
    String firstName;
    String lastName;
    boolean gender; //1 for man and 0 for women
    String avatar;
}
