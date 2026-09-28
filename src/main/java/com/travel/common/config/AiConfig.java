package com.travel.common.config;

import com.travel.flight.tools.FlightTools;
import com.travel.hotel.tool.HotelTools;
import com.travel.weather.tools.WeatherTools;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AiConfig {

    @Value("classpath:/prompts/travel-agent-system.st")
    private Resource systemPrompt;

    private final FlightTools flightTools;
    private final ChatMemory chatMemory;
    private final WeatherTools weatherTools;
    private final HotelTools hotelTools;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        SafeGuardAdvisor safeGuard = new SafeGuardAdvisor(
                List.of("hack", "bomb", "password"),
                "Sorry, I can only help with travel-related requests.",
                Ordered.HIGHEST_PRECEDENCE
        );
        return builder
                .defaultSystem(systemPrompt)
                .defaultTools(flightTools, hotelTools, weatherTools)
                .defaultAdvisors(
                        safeGuard,
                        MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}