package com.rafaelhosaka.shareme.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
public class Like {
    private String userId;

    public Like(String userId) {
        this.userId = userId;
    }
}
