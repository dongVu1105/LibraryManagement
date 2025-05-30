package com.dongVu1105.libraryManagement.controller;

import com.dongVu1105.libraryManagement.dto.request.PermissionRequest;
import com.dongVu1105.libraryManagement.dto.response.ApiResponse;
import com.dongVu1105.libraryManagement.dto.response.PermissionResponse;
import com.dongVu1105.libraryManagement.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest permissionRequest) {
        PermissionResponse permissionResponse = permissionService.create(permissionRequest);
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionResponse)
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAllPermission())
                .build();
    }

    @DeleteMapping("/{permissionName}")
    public ApiResponse<Void> delete(@PathVariable String permissionName) {
        permissionService.delete(permissionName);
        return ApiResponse.<Void>builder().build();
    }
}
