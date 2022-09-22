package com.rafaelhosaka.shareme.websocket;
import com.rafaelhosaka.shareme.user.UserProfile;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FriendInformation {
    private String targetUserId;
    private UserProfile friend;
}

