package com.dongVu1105.libraryManagement.service;

import com.dongVu1105.libraryManagement.entity.WebSocketSession;
import com.dongVu1105.libraryManagement.repository.WebSocketSessionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WebSocketSessionService {
    WebSocketSessionRepository webSocketSessionRepository;

    public WebSocketSession create (WebSocketSession webSocketSession){
        return webSocketSessionRepository.save(webSocketSession);
    }

    public void deleteSession (String sessionId){
        webSocketSessionRepository.deleteBySessionId (sessionId);
    }
}
