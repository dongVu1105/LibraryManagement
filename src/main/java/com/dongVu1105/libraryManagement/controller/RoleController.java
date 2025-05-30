package com.dongVu1105.libraryManagement.controller;

import com.dongVu1105.libraryManagement.dto.request.RoleRequest;
import com.dongVu1105.libraryManagement.dto.response.ApiResponse;
import com.dongVu1105.libraryManagement.dto.response.RoleResponse;
import com.dongVu1105.libraryManagement.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest roleRequest) {
        RoleResponse roleResponse = roleService.create(roleRequest);
        return ApiResponse.<RoleResponse>builder().result(roleResponse).build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{roleName}")
    public ApiResponse<Void> delete(@PathVariable String roleName) {
        roleService.delete(roleName);
        return ApiResponse.<Void>builder().build();
    }
}
