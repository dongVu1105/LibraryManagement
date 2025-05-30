package com.dongVu1105.libraryManagement.exception;

import com.dongVu1105.libraryManagement.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> HandlingRuntimeException (Exception e){
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED;
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
    }


    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> HandlingAppException (AppException appException){
        ErrorCode errorCode = appException.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(
                ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse> HandlingAccessDenied (AccessDeniedException e){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
    }
}
