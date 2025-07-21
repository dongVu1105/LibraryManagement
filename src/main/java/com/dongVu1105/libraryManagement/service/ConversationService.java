package com.dongVu1105.libraryManagement.service;

import com.dongVu1105.libraryManagement.dto.request.ConversationRequest;
import com.dongVu1105.libraryManagement.dto.response.ConversationResponse;
import com.dongVu1105.libraryManagement.entity.Conversation;
import com.dongVu1105.libraryManagement.entity.ParticipantInfo;
import com.dongVu1105.libraryManagement.entity.User;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.exception.ErrorCode;
import com.dongVu1105.libraryManagement.mapper.ConversationMapper;
import com.dongVu1105.libraryManagement.repository.ConversationRepository;
import com.dongVu1105.libraryManagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConversationService {
    UserRepository userRepository;
    ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;

    public List<ConversationResponse> myConversations () throws AppException {
        String myUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return conversationRepository.findAllByParticipantInfosContains(myUsername).stream()
                .map(this::toConversationResponse).toList();
    }

    public ConversationResponse create (ConversationRequest request) throws AppException {
        String myUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String participantUsername = request.getParticipantUsername().getFirst();
        User myProfile = userRepository.findByUsername(myUsername).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User participantProfile = userRepository.findByUsername(participantUsername).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<String> ids = new ArrayList<>();
        ids.add(myProfile.getId());
        ids.add(participantProfile.getId());
        List<String> sortedIds = ids.stream().sorted().toList();
        String conversationHash = generateHash(sortedIds);
        Conversation conversation = conversationRepository.findByConversationHash(conversationHash).orElseGet(() -> {

            List<ParticipantInfo> participantInfos = List.of(
                    ParticipantInfo.builder()
                            .username(myProfile.getUsername())
                            .gender(myProfile.isGender())
                            .firstName(myProfile.getFirstName())
                            .lastName(myProfile.getLastName())
                            .avatar(myProfile.getAvatar())
                            .build(),
                    ParticipantInfo.builder()
                            .username(participantProfile.getUsername())
                            .gender(participantProfile.isGender())
                            .firstName(participantProfile.getFirstName())
                            .lastName(participantProfile.getLastName())
                            .avatar(participantProfile.getAvatar())
                            .build()
            );
            Conversation newConversation = Conversation.builder()
                    .type(request.getType())
                    .participantInfos(participantInfos)
                    .conversationHash(conversationHash)
                    .createdDate(Instant.now())
                    .build();
            return conversationRepository.save(newConversation);
        });
        return toConversationResponse(conversation);
    }

    private String generateHash (List<String> sortedIds){
        StringJoiner stringJoiner = new StringJoiner("_");
        for(String id : sortedIds){
            stringJoiner.add(id);
        }
        return stringJoiner.toString();
    }

    private ConversationResponse toConversationResponse (Conversation conversation){
        String myUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        ConversationResponse conversationResponse = conversationMapper.toConversationResponse(conversation);
        conversationResponse.getParticipantInfos()
                .stream()
                .filter(participantInfo -> !participantInfo.getUsername().equals(myUsername))
                .findFirst()
                .ifPresent(participantInfo -> {
                    conversationResponse.setConversationAvatar(participantInfo.getAvatar());
                    conversationResponse.setConversationName(participantInfo.getUsername());
                });
        return conversationResponse;
    }
}
