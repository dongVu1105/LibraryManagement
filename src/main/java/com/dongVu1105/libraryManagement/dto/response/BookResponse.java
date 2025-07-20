package com.dongVu1105.libraryManagement.dto.response;

import com.dongVu1105.libraryManagement.entity.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {
    String id;
    String name;
    String author;
    String description;
    Long quantity;
    String category;
    String image;
}
