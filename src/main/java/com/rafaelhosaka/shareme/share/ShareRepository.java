package com.rafaelhosaka.shareme.share;

import com.rafaelhosaka.shareme.user.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ShareRepository extends MongoRepository<SharedPost,String> {
    @Query("{ 'user' : ?0}")
    List<SharedPost> findSharedPostByUserId(String userId);
}
