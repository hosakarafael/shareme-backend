package com.rafaelhosaka.shareme.notification;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "notification")
public abstract class Notification {
    @Id
    private String id;

    private boolean read = false;

    private String ownerUserId;

    private LocalDateTime dateCreated = LocalDateTime.now();
}
