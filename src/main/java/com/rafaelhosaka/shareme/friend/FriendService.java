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

    public boolean isRequested(String requestingUserId, String targetUserId) {
        List<FriendRequest> requestedUsers = friendRequestRepository.getRequestedUsers(requestingUserId);
        for (FriendRequest request: requestedUsers) {
            if(request.getTargetUserId().equals(targetUserId)){
                return true;
            }
        }
        return false;
    }

    public boolean isPending(String requestingUserId, String targetUserId) {
        List<FriendRequest> pendingUsers = friendRequestRepository.getPendingFriendRequest(targetUserId);
        for (FriendRequest request: pendingUsers) {
            if(request.getRequestingUserId().equals(requestingUserId)){
                return true;
            }
        }
        return false;
    }
}
