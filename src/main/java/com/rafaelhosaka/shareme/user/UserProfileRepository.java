package com.rafaelhosaka.shareme.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends MongoRepository<UserProfile, String> {

    Optional<UserProfile> findUserProfileByEmail(String email);

    @Query("{'$or': [ {'firstName' : { $regex: ?0, $options: 'i'  }}, {'lastName' : { $regex: ?0, $options: 'i'  }} ]}")
    List<UserProfile> searchUsersContainsName(String name);

}
