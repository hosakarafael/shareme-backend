package com.rafaelhosaka.shareme.post;

import com.rafaelhosaka.shareme.user.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<BasePost, String> {

    @Query("{ 'user.id' : ?0}")
    List<BasePost> getPostByUserId(String id);

    @Override
    List<BasePost> findAll();
}
