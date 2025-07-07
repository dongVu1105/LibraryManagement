package com.dongVu1105.libraryManagement.repository;

import com.dongVu1105.libraryManagement.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, String> {
    History findByUsernameAndBookID (String username, String bookID);
    List<History> findAllByUsername (String username);
    List<History> findAllByBookID (String bookID);
}
