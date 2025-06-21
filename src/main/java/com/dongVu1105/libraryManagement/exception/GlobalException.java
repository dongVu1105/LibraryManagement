package com.dongVu1105.libraryManagement.exception;

import com.dongVu1105.libraryManagement.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalException {

    private static final String MIN_ATTRIBUTE = "min";

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

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> HandlingVaidation (MethodArgumentNotValidException e){
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Objects> attributes = null;
        log.info(e.toString());
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolation =
                    e.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
        } catch (IllegalArgumentException exception){

        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
            Objects.nonNull(attributes)
                ? mapAttributes(errorCode.getMessage(), attributes)
                : errorCode.getMessage()
        );
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }

    public String mapAttributes (String message, Map<String, Objects> attributes){
        String valueOfAttributes = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{"+MIN_ATTRIBUTE+"}", valueOfAttributes);
    }
}
