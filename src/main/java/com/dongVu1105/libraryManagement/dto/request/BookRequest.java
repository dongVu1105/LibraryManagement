package com.dongVu1105.libraryManagement.dto.request;

import com.dongVu1105.libraryManagement.entity.Category;
import com.dongVu1105.libraryManagement.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {
    String id;
    String name;
    String author;
    String description;
    Long quantity;
    String category;
}
