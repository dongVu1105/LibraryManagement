package com.dongVu1105.libraryManagement.mapper;

import com.dongVu1105.libraryManagement.dto.request.BookRequest;
import com.dongVu1105.libraryManagement.dto.response.BookResponse;
import com.dongVu1105.libraryManagement.entity.Book;
import com.dongVu1105.libraryManagement.repository.BookRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toBook (BookRequest request);
    BookResponse toBookResponse (Book book);
    void updateBook (BookRequest request, @MappingTarget Book book);
}
