package com.dongVu1105.libraryManagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNAUTHENTICATED(1001, "authenticated error", HttpStatus.UNAUTHORIZED),
    USER_EXISTED(1002, "user existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1003, "user not existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1004, "role not existed", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1005, "You do not have permission", HttpStatus.FORBIDDEN),
    UNCATEGORIZED(1006, "uncategorized exception", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME (1007, "username must be at least {min} character", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1008, "password must be at least {min} character", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1009, "invalid key in validation", HttpStatus.BAD_REQUEST),
    INVALID_BIRTH(1010, "your age must be at least {min} years old", HttpStatus.BAD_REQUEST),
    BOOK_NOT_EXISTED(1011, "book not existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1012, "category not existed", HttpStatus.BAD_REQUEST),
    BOOK_EXISTED(1013, "book existed", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(1014, "category existed", HttpStatus.BAD_REQUEST),
    HISTORY_NOT_EXISTED(1015, "history not existed", HttpStatus.BAD_REQUEST),
    OUT_OF_BOOK(1016, "out of book", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY(1017, "quantity must be at least {min} books", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND (1018, "file not found", HttpStatus.NOT_FOUND)
    ;


    private long code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(long code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
