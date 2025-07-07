package com.dongVu1105.libraryManagement.controller;

import com.dongVu1105.libraryManagement.dto.request.AuthenticateRequest;
import com.dongVu1105.libraryManagement.dto.request.LogoutRequest;
import com.dongVu1105.libraryManagement.dto.request.RefreshTokenRequest;
import com.dongVu1105.libraryManagement.dto.response.ApiResponse;
import com.dongVu1105.libraryManagement.dto.response.AuthenticateResponse;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticateController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticateResponse> authenticate (@RequestBody AuthenticateRequest request) throws AppException {
        System.out.println("Da vao controller");
        return ApiResponse.<AuthenticateResponse>builder()
                .result(authenticationService.authenticate(request)).build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout (@RequestBody LogoutRequest request) throws AppException, ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticateResponse> refreshToken (@RequestBody RefreshTokenRequest request) throws AppException, ParseException, JOSEException {
        return ApiResponse.<AuthenticateResponse>builder().result(authenticationService.refreshToken(request)).build();
    }
}
