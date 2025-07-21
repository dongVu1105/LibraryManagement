package com.dongVu1105.libraryManagement.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.dongVu1105.libraryManagement.dto.request.AuthenticateRequest;
import com.dongVu1105.libraryManagement.dto.request.IntrospectRequest;
import com.dongVu1105.libraryManagement.dto.response.IntrospectResponse;
import com.dongVu1105.libraryManagement.entity.User;
import com.dongVu1105.libraryManagement.entity.WebSocketSession;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.exception.ErrorCode;
import com.dongVu1105.libraryManagement.repository.UserRepository;
import com.dongVu1105.libraryManagement.service.AuthenticationService;
import com.dongVu1105.libraryManagement.service.WebSocketSessionService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocketHandler {
    SocketIOServer socketIOServer;
    AuthenticationService authenticationService;
    WebSocketSessionService webSocketSessionService;
    UserRepository userRepository;

    @OnConnect
    public void clientConnect (SocketIOClient socketIOClient) throws AppException, ParseException, JOSEException {
        String token = socketIOClient.getHandshakeData().getSingleUrlParam("token");
        IntrospectResponse introspectResponse = authenticationService.introspect(
                IntrospectRequest.builder().accessToken(token).build());
        if(introspectResponse.isValid()){
            log.info("socketIOClient connected");
            String username = SignedJWT.parse(token).getJWTClaimsSet().getSubject();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            String userId = user.getId();
            webSocketSessionService.create(WebSocketSession.builder()
                    .sessionId(socketIOClient.getSessionId().toString())
                    .userId(userId)
                    .createdDate(Instant.now())
                    .build());
        } else {
            log.info("Authentication fail");
            socketIOClient.disconnect();
        }
    }

    @OnDisconnect
    public void clientDisconnect (SocketIOClient socketIOClient){
        webSocketSessionService.deleteSession(socketIOClient.getSessionId().toString());
        log.info("socketIOClient disconnected");
    }



    @PostConstruct
    public void startServer (){
        socketIOServer.start();
        socketIOServer.addListeners(this);
        log.info("Socket server started");
    }

    @PreDestroy
    public void stopServer (){
        socketIOServer.stop();
        log.info("Socket server stopped");
    }
}
