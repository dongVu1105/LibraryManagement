package com.dongVu1105.libraryManagement.controller;

import com.dongVu1105.libraryManagement.dto.request.CategoryRequest;
import com.dongVu1105.libraryManagement.dto.response.ApiResponse;
import com.dongVu1105.libraryManagement.dto.response.CategoryResponse;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryController {
    CategoryService categoryService;

    @PostMapping("/create")
    public ApiResponse<CategoryResponse> create (@RequestBody CategoryRequest request) throws AppException {
        return ApiResponse.<CategoryResponse>builder().result(categoryService.create(request)).build();
    }

    @GetMapping("/getAll")
    public ApiResponse<List<CategoryResponse>> getAll (){
        return ApiResponse.<List<CategoryResponse>>builder().result(categoryService.getList()).build();
    }

    @GetMapping("/getById/{id}")
    public ApiResponse<CategoryResponse> getById (@PathVariable("id") String id) throws AppException {
        return ApiResponse.<CategoryResponse>builder().result(categoryService.getCategoryById(id)).build();
    }

    @PutMapping("/update")
    public ApiResponse<CategoryResponse> update (@RequestBody CategoryRequest request) throws AppException {
        return ApiResponse.<CategoryResponse>builder().result(categoryService.updateCategory(request)).build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete (@PathVariable("id") String id){
        categoryService.deleteCategory(id);
        return ApiResponse.<Void>builder().build();
    }
}
