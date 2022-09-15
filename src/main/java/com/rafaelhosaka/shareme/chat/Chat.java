package com.rafaelhosaka.shareme.chat;

import com.rafaelhosaka.shareme.message.Message;
import com.rafaelhosaka.shareme.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Chat {
    @Id
    private String id;

    @DBRef
    private UserProfile owner;

    @DBRef
    private UserProfile friend;

    @DBRef
    private Message lastMessage;

    private boolean read;
}
