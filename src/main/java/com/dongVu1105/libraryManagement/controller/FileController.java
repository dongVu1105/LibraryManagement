package com.dongVu1105.libraryManagement.controller;

import com.dongVu1105.libraryManagement.dto.response.ApiResponse;
import com.dongVu1105.libraryManagement.dto.response.FileData;
import com.dongVu1105.libraryManagement.dto.response.FileResponse;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    FileService fileService;

    @PostMapping("/upload")
    public ApiResponse<FileResponse> upload (@RequestParam MultipartFile file) throws IOException {
        return ApiResponse.<FileResponse>builder().result(fileService.upload(file)).build();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download (@PathVariable("id") String id) throws AppException, IOException {
        FileData fileData = fileService.download(id);
        return ResponseEntity.<Resource>ok()
                .header(HttpHeaders.CONTENT_TYPE, fileData.getContentType())
                .body(fileData.getResource());
    }
}
