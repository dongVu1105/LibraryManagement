package com.dongVu1105.libraryManagement.controller;

import com.dongVu1105.libraryManagement.dto.request.BookRequest;
import com.dongVu1105.libraryManagement.dto.response.ApiResponse;
import com.dongVu1105.libraryManagement.dto.response.BookResponse;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookController {
    BookService bookService;

    @PostMapping("/create")
    public ApiResponse<BookResponse> create (@RequestBody BookRequest bookRequest) throws AppException {
        return ApiResponse.<BookResponse>builder().result(bookService.create(bookRequest)).build();
    }

    @PutMapping("/update")
    public ApiResponse<BookResponse> update (@RequestBody BookRequest bookRequest) throws AppException {
        return ApiResponse.<BookResponse>builder().result(bookService.update(bookRequest)).build();
    }

    @GetMapping("/getAll")
    public ApiResponse<List<BookResponse>> getAll () throws AppException {
        return ApiResponse.<List<BookResponse>>builder().result(bookService.getList()).build();
    }

    @GetMapping("/getById/{id}")
    public ApiResponse<BookResponse> getById (@PathVariable("id") String id) throws AppException {
        return ApiResponse.<BookResponse>builder().result(bookService.getBookByID(id)).build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete (@PathVariable("id") String id) throws AppException {
        bookService.deleteBook(id);
        return ApiResponse.<Void>builder().build();
    }
}
