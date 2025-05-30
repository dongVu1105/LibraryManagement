package com.dongVu1105.libraryManagement.dto.response;

import com.dongVu1105.libraryManagement.entity.Permission;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    String name;
    String description;

    Set<PermissionResponse> permissions;
}
