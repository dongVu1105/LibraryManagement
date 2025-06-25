package com.dongVu1105.libraryManagement.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryResponse {
    String id;
    LocalDate borrowDate;
    LocalDate returnDate;
    String createdBy;
    String modifiedBy;
    String username;
    String bookID;
    boolean returned;
}
