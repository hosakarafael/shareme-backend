package com.rafaelhosaka.shareme.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    public FriendService(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    public List<FriendRequest> getRequestedUsers(String requestingUserId){
        return friendRequestRepository.getRequestedUsers(requestingUserId);
    }

    public List<FriendRequest> getPendingFriendRequest(String targetUserId){
        return friendRequestRepository.getPendingFriendRequest(targetUserId);
    }

    public FriendRequest createFriendRequest(FriendRequest friendRequest){
        return friendRequestRepository.save(friendRequest);
    }

    public void deleteFriendRequest(FriendRequest friendRequest) {
        friendRequestRepository.delete(friendRequest);
    }

    public FriendRequest getFriendRequestFromIds(String targetUserId, String requestingUserId) {
        return friendRequestRepository.getFriendRequestFromIds(targetUserId, requestingUserId);
    }
}
