package com.rafaelhosaka.shareme.friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(name = "friend-request_unique_idx", def = "{'targetUserId': 1, 'requestingUserId': 1}",
        unique = true)
public class FriendRequest {
    @Id
    private String id;
    private String targetUserId;
    private String requestingUserId;
}
