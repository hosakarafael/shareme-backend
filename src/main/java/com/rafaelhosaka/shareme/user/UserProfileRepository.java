package com.rafaelhosaka.shareme.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends MongoRepository<UserProfile, String> {

    Optional<UserProfile> findUserProfileByEmail(String email);

}
