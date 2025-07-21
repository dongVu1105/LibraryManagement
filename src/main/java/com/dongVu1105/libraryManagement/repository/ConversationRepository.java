package com.dongVu1105.libraryManagement.repository;

import com.dongVu1105.libraryManagement.entity.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
    Optional<Conversation> findById(String conversationId);

    Optional<Conversation> findByConversationHash(String conversationHash);
    @Query("{'participantInfos.username' : ?0}")
    List<Conversation> findAllByParticipantInfosContains (String username);
}
