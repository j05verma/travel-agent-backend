package com.travel.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "token_usage")
@CompoundIndex(def = "{'userKey': 1, 'date': 1}", unique = true)
public class TokenUsage {
    @Id
    private String id;
    private String userKey; // sessionId
    private String date;
    private long tokenUsed;
}
