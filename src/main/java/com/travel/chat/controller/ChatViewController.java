package com.travel.chat.controller;

import com.travel.chat.model.ChatMessage;
import com.travel.chat.service.ChatService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatViewController {

    private final ChatService chatService;

    @GetMapping("/")
    public String chatPage(HttpSession session, Model model) {
        String sessionId = getOrCreateSessionId(session);
        List<ChatMessage> history = chatService.getHistory(sessionId);
        model.addAttribute("history", history);
        return "chat";
    }

    @PostMapping("/chat")
    public String chat(@RequestParam String message, HttpSession session, Model model) {
        String sessionId = getOrCreateSessionId(session);
        chatService.sendMessage(sessionId, message);

        List<ChatMessage> history = chatService.getHistory(sessionId);
        model.addAttribute("history", history);
        return "chat";
    }

    private String getOrCreateSessionId(HttpSession session) {
        String sessionId = (String) session.getAttribute("chatSessionId");
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute("chatSessionId", sessionId);
        }
        return sessionId;
    }
}