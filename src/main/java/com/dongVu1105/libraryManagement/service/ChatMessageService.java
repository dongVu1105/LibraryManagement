package com.dongVu1105.libraryManagement.service;

import com.dongVu1105.libraryManagement.dto.request.ChatMessageRequest;
import com.dongVu1105.libraryManagement.dto.response.ChatMessageResponse;
import com.dongVu1105.libraryManagement.entity.ChatMessage;
import com.dongVu1105.libraryManagement.entity.Conversation;
import com.dongVu1105.libraryManagement.entity.ParticipantInfo;
import com.dongVu1105.libraryManagement.entity.User;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.exception.ErrorCode;
import com.dongVu1105.libraryManagement.mapper.ChatMessageMapper;
import com.dongVu1105.libraryManagement.repository.ChatMessageRepository;
import com.dongVu1105.libraryManagement.repository.ConversationRepository;
import com.dongVu1105.libraryManagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatMessageService {
    ChatMessageRepository chatMessageRepository;
    ChatMessageMapper chatMessageMapper;
    ConversationRepository conversationRepository;
    UserRepository userRepository;
    DateTimeFormat dateTimeFormat;

    public List<ChatMessageResponse> getMessage (String conversationId) throws AppException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(
                () -> new AppException(ErrorCode.CONVERSATION_NOT_EXISTED));
        conversation.getParticipantInfos().stream()
                .filter(participantInfo -> username.equals(participantInfo.getUsername()))
                .findAny()
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_EXISTED));
        List<ChatMessage> chatMessages = chatMessageRepository
                .findAllByConversationIdOrderByCreatedDateDesc(conversationId);
        return chatMessages.stream()
                .map(this::toChatMessageResponse).toList();
    }

    public ChatMessageResponse create (ChatMessageRequest request) throws AppException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Conversation conversation = conversationRepository.findById(request.getConversationId()).orElseThrow(
                () -> new AppException(ErrorCode.CONVERSATION_NOT_EXISTED));
        conversation.getParticipantInfos().stream()
                .filter(participantInfo -> username.equals(participantInfo.getUsername()))
                .findAny()
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_EXISTED));
        User myInfo = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        ChatMessage chatMessage = ChatMessage.builder()
                .message(request.getMessage())
                .conversationId(request.getConversationId())
                .createdDate(Instant.now())
                .sender(ParticipantInfo.builder()
                        .username(myInfo.getUsername())
                        .firstName(myInfo.getFirstName())
                        .lastName(myInfo.getLastName())
                        .gender(myInfo.isGender())
                        .avatar(myInfo.getAvatar())
                        .build())
                .build();
        chatMessage = chatMessageRepository.save(chatMessage);
        return toChatMessageResponse(chatMessage);
    }

    private ChatMessageResponse toChatMessageResponse (ChatMessage chatMessage){
        ChatMessageResponse chatMessageResponse = chatMessageMapper.toChatMessageResponse(chatMessage);
        chatMessageResponse.setCreatedDate(dateTimeFormat.format(chatMessage.getCreatedDate()));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        chatMessageResponse.setMe(username.equals(chatMessageResponse.getSender().getUsername()));
        return chatMessageResponse;
    }
}
