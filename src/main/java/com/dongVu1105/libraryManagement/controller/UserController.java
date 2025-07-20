package com.dongVu1105.libraryManagement.controller;

import com.dongVu1105.libraryManagement.dto.request.AccountCreationByAdminRequest;
import com.dongVu1105.libraryManagement.dto.request.UserCreationRequest;
import com.dongVu1105.libraryManagement.dto.request.UserUpdateRequest;
import com.dongVu1105.libraryManagement.dto.response.ApiResponse;
import com.dongVu1105.libraryManagement.dto.response.UserResponse;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> createUser (@RequestBody @Valid UserCreationRequest request) throws AppException {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request)).build();
    }

    @PostMapping("/createAccount")
    public ApiResponse<UserResponse> createAccountByAdmin (@RequestBody @Valid AccountCreationByAdminRequest request)
            throws AppException {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createAccountByAdmin(request)).build();
    }

    @GetMapping("/getUsers")
    public ApiResponse<List<UserResponse>> getAllUsers (){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUser()).build();
    }

    @GetMapping("/id/{userId}")
    public ApiResponse<UserResponse> getUserById (@PathVariable("userId") String userId) throws AppException {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(userId)).build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo () throws AppException {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo()).build();
    }

    @PutMapping("/updateMyInfo")
    public ApiResponse<UserResponse> updateUser
            (@RequestBody @Valid UserUpdateRequest request) throws AppException {
        return ApiResponse.<UserResponse>builder().result(userService.updateUser(request)).build();
    }

    @GetMapping("/username/{username}")
    public ApiResponse<UserResponse> getUserByUsername
            (@PathVariable("username") String username) throws AppException {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserByUsername(username)).build();
    }
    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser (@PathVariable("userId") String id){
        userService.deleteUser(id);
        return ApiResponse.<Void>builder().build();
    }

    @PutMapping("/avatar")
    public ApiResponse<UserResponse> updateAvatar (@RequestParam MultipartFile file) throws AppException, IOException {
        return ApiResponse.<UserResponse>builder().result(userService.updateAvatar(file)).build();
    }
}
