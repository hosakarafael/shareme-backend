package com.rafaelhosaka.shareme.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface UserProfileRepository extends MongoRepository<UserProfile, UUID> {
}
