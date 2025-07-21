package com.dongVu1105.libraryManagement.dto.response;

import com.dongVu1105.libraryManagement.entity.ParticipantInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageResponse {
    String id;
    String message;
    String createdDate;
    ParticipantInfo sender;
    String conversationId;
    boolean me;
}
