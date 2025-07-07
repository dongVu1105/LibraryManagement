package com.dongVu1105.libraryManagement.controller;

import com.dongVu1105.libraryManagement.dto.request.HistoryRequest;
import com.dongVu1105.libraryManagement.dto.response.ApiResponse;
import com.dongVu1105.libraryManagement.dto.response.HistoryResponse;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.service.HistoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class HistoryController {
    HistoryService historyService;

    @PostMapping("/create")
    public ApiResponse<HistoryResponse> create (@RequestBody HistoryRequest request) throws AppException {
        return ApiResponse.<HistoryResponse>builder().result(historyService.create(request)).build();
    }

    @PutMapping("/update")
    public ApiResponse<HistoryResponse> update (@RequestBody HistoryRequest request) throws AppException {
        return ApiResponse.<HistoryResponse>builder().result(historyService.update(request)).build();
    }

    @GetMapping("/getAll")
    public ApiResponse<List<HistoryResponse>> getAll () {
        return ApiResponse.<List<HistoryResponse>>builder().result(historyService.getAll()).build();
    }

    @GetMapping("/getByUsername/{username}")
    public ApiResponse<List<HistoryResponse>> getAllByUsername (@PathVariable("username") String username) {
        return ApiResponse.<List<HistoryResponse>>builder().result(historyService.getAllByUsername(username)).build();
    }

    @GetMapping("/getByBookID/{bookID}")
    public ApiResponse<List<HistoryResponse>> getAllByBookID (@PathVariable("bookID") String bookID) {
        return ApiResponse.<List<HistoryResponse>>builder().result(historyService.getAllByBookID(bookID)).build();
    }

    @GetMapping("/getMyHistory")
    public ApiResponse<List<HistoryResponse>> getMyHistory () {
        return ApiResponse.<List<HistoryResponse>>builder().result(historyService.getHistoryByUser()).build();
    }

}
