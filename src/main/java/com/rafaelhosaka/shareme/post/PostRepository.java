package com.rafaelhosaka.shareme.post;

import com.rafaelhosaka.shareme.user.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<BasePost, String> {

    @Query("{'$and' : [ { 'user.id' : ?0} , {'visibility.type' : 'PUBLIC'} ] }")
    List<BasePost> getPostByUserId(String id);

    @Query("{'$and' : [ { 'user.id' : ?0} , {'visibility.type' : 'GROUP'} ] }")
    List<BasePost> getGroupPostByUserId(String id);

    @Override
    List<BasePost> findAll();

    @Query("{'$and' : [ {'visibility.type': 'GROUP'} , { 'visibility.allowedIds' : {'$in': [?0]} } ]}")
    List<BasePost> getGroupPosts(String groupId);
}
