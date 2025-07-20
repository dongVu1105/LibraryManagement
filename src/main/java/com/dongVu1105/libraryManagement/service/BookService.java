package com.dongVu1105.libraryManagement.service;

import com.dongVu1105.libraryManagement.dto.request.BookRequest;
import com.dongVu1105.libraryManagement.dto.request.FileInfo;
import com.dongVu1105.libraryManagement.dto.response.BookResponse;
import com.dongVu1105.libraryManagement.dto.response.UserResponse;
import com.dongVu1105.libraryManagement.entity.Book;
import com.dongVu1105.libraryManagement.entity.Category;
import com.dongVu1105.libraryManagement.entity.FileManagement;
import com.dongVu1105.libraryManagement.entity.User;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.exception.ErrorCode;
import com.dongVu1105.libraryManagement.mapper.BookMapper;
import com.dongVu1105.libraryManagement.mapper.FileMapper;
import com.dongVu1105.libraryManagement.repository.BookRepository;
import com.dongVu1105.libraryManagement.repository.CategoryRepository;
import com.dongVu1105.libraryManagement.repository.FileManagementRepository;
import com.dongVu1105.libraryManagement.repository.FileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookService {
    BookRepository bookRepository;
    BookMapper bookMapper;
    CategoryRepository categoryRepository;
    FileRepository fileRepository;
    FileManagementRepository fileManagementRepository;
    FileMapper fileMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public BookResponse create (BookRequest request) throws AppException {
        if(bookRepository.existsById(request.getId())){
            throw new AppException(ErrorCode.BOOK_EXISTED);
        }
        Book book = bookMapper.toBook(request);
        categoryRepository.findById(request.getCategory()).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    public List<BookResponse> getList (){
        return bookRepository.findAll().stream().map(bookMapper::toBookResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public BookResponse update (BookRequest request) throws AppException {
        Book book = bookRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_EXISTED));
        categoryRepository.findById(request.getCategory()).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        bookMapper.updateBook(request, book);
        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    public BookResponse getBookByID (String id) throws AppException {
        Book book = bookRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_EXISTED));
        return bookMapper.toBookResponse(book);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBook (String id){
        bookRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public BookResponse updateImage (MultipartFile file, String bookId) throws AppException, IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_EXISTED));
        FileInfo fileInfo = fileRepository.store(file);
        FileManagement fileManagement = fileMapper.toFileManagement(fileInfo);
        fileManagement.setUsername(username);
        fileManagementRepository.save(fileManagement);
        book.setImage(fileInfo.getUrl());
        return bookMapper.toBookResponse(bookRepository.save(book));
    }
}
