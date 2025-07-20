package com.dongVu1105.libraryManagement.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookSearchRequest {
    int page;
    int size;
    String keyword;
}
