package com.dongVu1105.libraryManagement.dto.response;

import com.dongVu1105.libraryManagement.entity.ParticipantInfo;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationResponse {
    String id;
    String type;
    String conversationHash;
    List<ParticipantInfo> participantInfos;
    String conversationName;
    String conversationAvatar;
    Instant createdDate;
}
