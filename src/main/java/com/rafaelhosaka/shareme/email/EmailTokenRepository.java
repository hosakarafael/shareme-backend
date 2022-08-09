package com.rafaelhosaka.shareme.email;

import com.rafaelhosaka.shareme.applicationuser.ApplicationUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface EmailTokenRepository extends MongoRepository<EmailToken, String> {
    @Query("{'token' : ?0 }")
    Optional<EmailToken> getEmailTokenByToken(String token);

    @Query("{'user.id' : ?0 }")
    Optional<EmailToken> getEmailTokenByUserId(String id);
}
