package com.dongVu1105.libraryManagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageRequest {
    @NotBlank
    String message;
    @NotBlank
    String conversationId;
}
