package com.travel.chat.controller;

import com.travel.chat.model.ChatMessage;
import com.travel.chat.service.ChatService;
import com.travel.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatApiController {

    private final ChatService chatService;

    @PostMapping
    public ApiResponse<String> chat(@RequestParam(required = false) String sessionId,
                                     @RequestParam String message) {
        String actualSessionId = (sessionId != null) ? sessionId : UUID.randomUUID().toString();
        String reply = chatService.sendMessage(actualSessionId, message);
        return ApiResponse.ok(reply, "sessionId: " + actualSessionId);
    }

    @GetMapping("/history/{sessionId}")
    public ApiResponse<List<ChatMessage>> getHistory(@PathVariable String sessionId) {
        List<ChatMessage> history = chatService.getHistory(sessionId);
        return ApiResponse.ok(history, "History fetched successfully");
    }
}