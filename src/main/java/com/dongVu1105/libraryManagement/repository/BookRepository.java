package com.dongVu1105.libraryManagement.repository;

import com.dongVu1105.libraryManagement.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Page<Book> findAllByNameContaining (String keyword, Pageable pageable);
}
