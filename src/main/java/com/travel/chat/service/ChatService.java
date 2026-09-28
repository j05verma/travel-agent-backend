package com.travel.chat.service;

import com.travel.chat.model.ChatMessage;
import com.travel.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final String LIMIT_MSG =
            "You've reached your daily token limit. Please try again tomorrow.";

    private final TokenService tokenService;
    private final ChatClient chatClient;
    private final ChatMessageRepository chatMessageRepository;

    public String sendMessage(String sessionId, String query) {
        //  Limit check
        if (tokenService.isLimitExceeded(sessionId)){
            save(sessionId, query, LIMIT_MSG);
            return LIMIT_MSG;
        }
        // AI call
        ChatResponse response = chatClient.prompt()
                .user(query)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .call()
                .chatResponse();
        String reply = response.getResult().getOutput().getText();

        //  Usage count
        Usage usage = response.getMetadata().getUsage();
        if (usage != null && usage.getTotalTokens() != null) {
            tokenService.addUsage(sessionId, usage.getTotalTokens());
        }

        save(sessionId, query, reply);
        return reply;
    }

    private void save(String sessionId, String query, String reply) {
        chatMessageRepository.save(ChatMessage.builder()
                .sessionId(sessionId)
                .userMessage(query)
                .aiReply(reply)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public List<ChatMessage> getHistory(String sessionId) {
        return chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
    }
}