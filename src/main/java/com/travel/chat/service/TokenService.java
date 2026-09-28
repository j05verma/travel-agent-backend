package com.travel.chat.service;

import com.travel.chat.model.TokenUsage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final MongoTemplate mongoTemplate;

    @Value("${app.ai.daily-token-limit:5000}")
    private long dailyLimit;

    private Query todayQuery(String userKey){
        String today = LocalDate.now(ZoneId.of("Asia/Kolkata")).toString();
        return Query.query(Criteria.where("userKey").is(userKey).and("date").is(today));
    }

    public boolean isLimitExceeded(String userKey){
        TokenUsage usage = mongoTemplate.findOne(todayQuery(userKey), TokenUsage.class);
        return usage != null && usage.getTokenUsed() > dailyLimit;
    }

    public void addUsage(String userKey, long tokens){
        mongoTemplate.upsert(todayQuery(userKey),
                new Update().inc("tokenUsed", tokens), TokenUsage.class
        );
    }

}
