package com.rafaelhosaka.shareme.applicationuser;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ApplicationUserRepository extends MongoRepository<ApplicationUser, String> {
    @Query("{ 'username' : ?0 }")
    Optional<ApplicationUser> findByUsername(String username);
}
