package com.dongVu1105.libraryManagement.service;

import com.dongVu1105.libraryManagement.dto.request.FileInfo;
import com.dongVu1105.libraryManagement.dto.response.FileData;
import com.dongVu1105.libraryManagement.dto.response.FileResponse;
import com.dongVu1105.libraryManagement.entity.FileManagement;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.exception.ErrorCode;
import com.dongVu1105.libraryManagement.mapper.FileMapper;
import com.dongVu1105.libraryManagement.repository.FileManagementRepository;
import com.dongVu1105.libraryManagement.repository.FileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FileService {

    FileManagementRepository fileManagementRepository;
    FileRepository fileRepository;
    FileMapper fileMapper;

    public FileResponse upload (MultipartFile file) throws IOException {
        FileInfo fileInfo = fileRepository.store(file);
        FileManagement fileManagement = fileMapper.toFileManagement(fileInfo);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        fileManagement.setUsername(username);
        fileManagementRepository.save(fileManagement);
        return FileResponse.builder()
                .url(fileInfo.getUrl())
                .originalName(file.getOriginalFilename())
                .build();
    }

    public FileData download (String fileName) throws AppException, IOException {
        FileManagement fileManagement = fileManagementRepository.findById(fileName).orElseThrow(
                () -> new AppException(ErrorCode.FILE_NOT_FOUND));
        Resource resource = fileRepository.read(fileManagement);
        return FileData.builder()
                .resource(resource)
                .contentType(fileManagement.getContentType())
                .build();


    }
}
