package com.rafaelhosaka.shareme.notification;

import com.rafaelhosaka.shareme.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notification")
public class FriendAcceptedNotification extends Notification{
    @DBRef
    private UserProfile acceptedFriend;
}