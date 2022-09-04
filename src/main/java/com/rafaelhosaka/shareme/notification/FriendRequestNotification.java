package com.rafaelhosaka.shareme.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notification")
public class FriendRequestNotification extends Notification{
    private NotificationUserData friendRequesting;
}
