package com.rafaelhosaka.shareme.friend;

import com.rafaelhosaka.shareme.user.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {
    @Query("{ 'requestingUserId' : ?0 }")
    List<FriendRequest> getRequestedUsers(String requestingUserId);

    @Query("{ 'targetUserId' : ?0 }")
    List<FriendRequest> getPendingFriendRequest(String targetUserId);

    @Query("{ 'targetUserId' : ?0, 'requestingUserId' : ?1 }")
    FriendRequest getFriendRequestFromIds(String targetUserId, String requestingUserId);
}
