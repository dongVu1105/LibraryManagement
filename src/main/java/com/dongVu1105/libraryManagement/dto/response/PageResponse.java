package com.dongVu1105.libraryManagement.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {
    int page;
    int size;
    int totalPages;
    long totalElements;

    @Builder.Default
    private List<T> data = Collections.emptyList();
}
