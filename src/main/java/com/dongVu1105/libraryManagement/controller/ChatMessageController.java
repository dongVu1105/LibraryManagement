package com.dongVu1105.libraryManagement.controller;

import com.dongVu1105.libraryManagement.dto.request.ChatMessageRequest;
import com.dongVu1105.libraryManagement.dto.response.ApiResponse;
import com.dongVu1105.libraryManagement.dto.response.ChatMessageResponse;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat-message")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageController {
    ChatMessageService chatMessageService;

    @PostMapping("/create")
    public ApiResponse<ChatMessageResponse> create (@RequestBody @Valid ChatMessageRequest request) throws AppException {
        return ApiResponse.<ChatMessageResponse>builder().result(chatMessageService.create(request)).build();
    }

    @GetMapping("/my-messages")
    public ApiResponse<List<ChatMessageResponse>> myMessages (@RequestParam String conversationId) throws AppException {
        return ApiResponse.<List<ChatMessageResponse>>builder().result(chatMessageService.getMessage(conversationId)).build();
    }
}
