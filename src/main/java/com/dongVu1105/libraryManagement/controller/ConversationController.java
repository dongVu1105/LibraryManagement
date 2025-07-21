package com.dongVu1105.libraryManagement.controller;

import com.dongVu1105.libraryManagement.dto.request.ConversationRequest;
import com.dongVu1105.libraryManagement.dto.response.ApiResponse;
import com.dongVu1105.libraryManagement.dto.response.ConversationResponse;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.repository.ConversationRepository;
import com.dongVu1105.libraryManagement.service.ConversationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConversationController {
    ConversationService conversationService;

    @PostMapping("/create")
    public ApiResponse<ConversationResponse> create (@RequestBody @Valid ConversationRequest request) throws AppException {
        return ApiResponse.<ConversationResponse>builder().result(conversationService.create(request)).build();
    }

    @GetMapping("/my-conversations")
    public ApiResponse<List<ConversationResponse>> myConversations () throws AppException {
        return ApiResponse.<List<ConversationResponse>>builder().result(conversationService.myConversations()).build();
    }
}
