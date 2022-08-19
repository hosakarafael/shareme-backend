package com.rafaelhosaka.shareme.message;


import com.rafaelhosaka.shareme.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Message implements Comparable<Message> {
    @Id
    private String id;
    private UserProfile sender;
    private UserProfile receiver;
    private String content;
    private LocalDateTime dateSent;

    @Override
    public int compareTo(Message o) {
        return getDateSent().compareTo(o.getDateSent());
    }
}
