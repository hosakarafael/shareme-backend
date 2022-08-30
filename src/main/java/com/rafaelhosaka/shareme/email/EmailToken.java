package com.rafaelhosaka.shareme.email;

import com.rafaelhosaka.shareme.applicationuser.ApplicationUser;
import com.rafaelhosaka.shareme.user.UserProfile;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class EmailToken {
    private static final int EXPIRATION_HOUR =  24;

    @Id
    private String id;

    private String token;

    @DBRef
    @Indexed(unique = true)
    private ApplicationUser user;

    private LocalDateTime expiryDate;

    public EmailToken(){
        expiryDate = LocalDateTime.now().plusHours(EXPIRATION_HOUR);
    }

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }

}
