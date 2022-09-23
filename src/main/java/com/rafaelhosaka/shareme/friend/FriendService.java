package com.rafaelhosaka.shareme.friend;

import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import com.rafaelhosaka.shareme.notification.FriendRequestNotification;
import com.rafaelhosaka.shareme.notification.Notification;
import com.rafaelhosaka.shareme.notification.NotificationService;
import com.rafaelhosaka.shareme.user.UserProfile;
import com.rafaelhosaka.shareme.user.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendService {
    private FriendRequestRepository friendRequestRepository;
    private UserProfileService userProfileService;
    private NotificationService notificationService;

    @Autowired
    public FriendService(FriendRequestRepository friendRequestRepository, UserProfileService userProfileService, NotificationService notificationService) {
        this.friendRequestRepository = friendRequestRepository;
        this.userProfileService = userProfileService;
        this.notificationService = notificationService;
    }

    public List<FriendRequest> getRequestedUsers(String requestingUserId){
        return friendRequestRepository.getRequestedUsers(requestingUserId);
    }

    public List<FriendRequest> getPendingFriendRequest(String targetUserId){
        return friendRequestRepository.getPendingFriendRequest(targetUserId);
    }

    public List<Object> createFriendRequest(FriendRequest friendRequest) throws UserProfileNotFoundException {
        List<Object> returnData = new ArrayList<>();
        Notification notification = notificationService.createFriendRequestNotification(friendRequest);
        FriendRequest request = friendRequestRepository.save(friendRequest);
        returnData.add(request);
        returnData.add(notification);
        return returnData;
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

    public List<Object> acceptRequest(FriendRequest friendRequest) throws UserProfileNotFoundException {
        if(!friendRequestRepository.findById(friendRequest.getId()).isPresent()) {
            throw new IllegalStateException("FriendRequest does not exist");
        }

        UserProfile requestingUser = userProfileService.getUserProfileById(friendRequest.getRequestingUserId());
        UserProfile targetUser = userProfileService.getUserProfileById(friendRequest.getTargetUserId());

        if(requestingUser.getFriends().contains(targetUser.getId()) || targetUser.getFriends().contains(requestingUser.getId())){
            throw new IllegalStateException("Users are already friends");
        }

        requestingUser.getFriends().add(targetUser.getId());
        targetUser.getFriends().add(requestingUser.getId());

        userProfileService.update(requestingUser);
        userProfileService.update(targetUser);

        deleteFriendRequest(friendRequest);
        Notification notification = notificationService.createFriendAcceptedNotification(friendRequest);

        List<Object> returnData = new ArrayList<>();

        returnData.add(requestingUser);
        returnData.add(targetUser);
        returnData.add(notification);

        return returnData;
    }

    public List<UserProfile> unfriend(UserProfile user1, UserProfile user2) {
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());

        userProfileService.update(user1);
        userProfileService.update(user2);

        List<UserProfile> modifiedUsers = new ArrayList<>();

        modifiedUsers.add(user1);
        modifiedUsers.add(user2);

        return modifiedUsers;
    }
}
