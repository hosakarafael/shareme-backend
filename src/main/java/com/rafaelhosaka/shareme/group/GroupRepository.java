package com.rafaelhosaka.shareme.group;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
    @Query("{'$or':[ {'admins': {'$in': [?0]} }, {'members': {'$in' : [?0]}} ] }")
    List<Group> getGroupsByUserId(String userId);
}
