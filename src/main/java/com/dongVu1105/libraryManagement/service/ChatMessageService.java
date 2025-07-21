package com.dongVu1105.libraryManagement.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.dongVu1105.libraryManagement.dto.request.ChatMessageRequest;
import com.dongVu1105.libraryManagement.dto.response.ChatMessageResponse;
import com.dongVu1105.libraryManagement.entity.*;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.exception.ErrorCode;
import com.dongVu1105.libraryManagement.mapper.ChatMessageMapper;
import com.dongVu1105.libraryManagement.repository.ChatMessageRepository;
import com.dongVu1105.libraryManagement.repository.ConversationRepository;
import com.dongVu1105.libraryManagement.repository.UserRepository;
import com.dongVu1105.libraryManagement.repository.WebSocketSessionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

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
    WebSocketSessionRepository webSocketSessionRepository;
    SocketIOServer socketIOServer;
    ObjectMapper objectMapper;

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
        ChatMessageResponse chatMessageResponse = chatMessageMapper.toChatMessageResponse(
                chatMessageRepository.save(chatMessage));
        List<String> participantIds = new ArrayList<>();
        for(ParticipantInfo participantInfo : conversation.getParticipantInfos()){
            User user = userRepository.findByUsername(participantInfo.getUsername()).orElseThrow(
                    () -> new AppException(ErrorCode.USER_NOT_EXISTED));
            participantIds.add(user.getId());
        }
        List<WebSocketSession> webSocketSessions = webSocketSessionRepository.findAllByUserIdIn(participantIds);
        Map<String, WebSocketSession> lookup = new TreeMap<>();
        for(WebSocketSession webSocketSession : webSocketSessions){
            lookup.put(webSocketSession.getSessionId(), webSocketSession);
        }
        socketIOServer.getAllClients().forEach(socketIOClient -> {
            WebSocketSession webSocketSession = lookup.get(socketIOClient.getSessionId().toString());
            if(Objects.nonNull(webSocketSession)){
                String message = null;
                try {
                    chatMessageResponse.setMe(myInfo.getId().equals(webSocketSession.getUserId()));
                    message = objectMapper.writeValueAsString(chatMessageResponse);
                    socketIOClient.sendEvent("message", message);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
