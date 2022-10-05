package com.rafaelhosaka.shareme.group;

import com.rafaelhosaka.shareme.user.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
    @Query("{'$or':[ {'admins': {'$in': [?0]} }, {'members': {'$in' : [?0]}} ] }")
    List<Group> getGroupsByUserId(String userId);

    @Query("{ 'name' : { $regex: ?0, $options: 'i'  }}")
    List<Group> searchGroupsContainsName(String name);
}
