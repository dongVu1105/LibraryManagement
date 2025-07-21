package com.dongVu1105.libraryManagement.repository;

import com.dongVu1105.libraryManagement.entity.WebSocketSession;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebSocketSessionRepository extends MongoRepository<WebSocketSession, String> {
    void deleteBySessionId(String sessionId);
    List<WebSocketSession> findAllByUserIdIn (List<String> userIds);
}
