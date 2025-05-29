package com.dongVu1105.libraryManagement.controller;

import com.dongVu1105.libraryManagement.dto.request.AuthenticateRequest;
import com.dongVu1105.libraryManagement.dto.response.ApiResponse;
import com.dongVu1105.libraryManagement.dto.response.AuthenticateResponse;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticateController {
    AuthenticationService authenticationService;

    @PostMapping("login")
    ApiResponse<AuthenticateResponse> authenticate (@RequestBody AuthenticateRequest request) throws AppException {
        return ApiResponse.<AuthenticateResponse>builder()
                .result(authenticationService.authenticate(request)).build();
    }
}
