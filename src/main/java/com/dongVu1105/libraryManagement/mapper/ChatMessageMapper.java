package com.dongVu1105.libraryManagement.mapper;

import com.dongVu1105.libraryManagement.dto.request.ChatMessageRequest;
import com.dongVu1105.libraryManagement.dto.response.ChatMessageResponse;
import com.dongVu1105.libraryManagement.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper  {
    ChatMessageResponse toChatMessageResponse (ChatMessage chatMessage);
}
