package com.rafaelhosaka.shareme.post;

import com.rafaelhosaka.shareme.user.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

    @Query("{ 'user.id' : ?0}")
    List<Post> getPostByUserId(String id);
}
