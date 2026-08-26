package com.travel.chat.service;

import com.travel.chat.model.ChatMessage;
import com.travel.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;
    private final ChatMessageRepository chatMessageRepository;

    public String sendMessage(String sessionId, String query) {
        String reply = chatClient.prompt()
                .user(query)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .call()
                .content();

        ChatMessage message = ChatMessage.builder()
                .sessionId(sessionId)
                .userMessage(query)
                .aiReply(reply)
                .timestamp(LocalDateTime.now())
                .build();
        chatMessageRepository.save(message);

        return reply;
    }

    public List<ChatMessage> getHistory(String sessionId) {
        return chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
    }
}