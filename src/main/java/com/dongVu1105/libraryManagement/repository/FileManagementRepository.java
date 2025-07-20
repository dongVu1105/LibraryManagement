package com.dongVu1105.libraryManagement.repository;

import com.dongVu1105.libraryManagement.entity.FileManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileManagementRepository extends JpaRepository<FileManagement, String> {
    Optional<FileManagement> findById(String id);
}
