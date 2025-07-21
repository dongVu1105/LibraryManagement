package com.dongVu1105.libraryManagement.mapper;

import com.dongVu1105.libraryManagement.dto.response.ConversationResponse;
import com.dongVu1105.libraryManagement.entity.Conversation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    ConversationResponse toConversationResponse (Conversation conversation);
}
