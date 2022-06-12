package com.rafaelhosaka.shareme.friend;

import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import com.rafaelhosaka.shareme.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/friend")
public class FriendController {
    private FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("/requested/{requestingUserId}")
    public ResponseEntity<List<FriendRequest>> getRequestedUsers(@PathVariable("requestingUserId") String requestingUserId){
        return ResponseEntity.ok().body(friendService.getRequestedUsers(requestingUserId));
    }

    @GetMapping("/pending/{targetUserId}")
    public ResponseEntity<List<FriendRequest>> getPendingFriendRequest(@PathVariable("targetUserId") String targetUserId){
        return ResponseEntity.ok().body(friendService.getPendingFriendRequest(targetUserId));
    }

    @PostMapping("/createRequest")
    public ResponseEntity<FriendRequest> createFriendRequest(@RequestBody FriendRequest friendRequest){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/friend/createRequest").toUriString());
        return ResponseEntity.created(uri).body(friendService.createFriendRequest(friendRequest));
    }
    
    @DeleteMapping("/deleteRequest")
    public ResponseEntity deleteFriendRequest(@RequestBody FriendRequest friendRequest){
        friendService.deleteFriendRequest(friendRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getRequest")
    public ResponseEntity<FriendRequest> getFriendRequestFromIds(@RequestParam("targetUserId") String targetUserId, @RequestParam("requestingUserId") String requestingUserId){
        return ResponseEntity.ok().body(friendService.getFriendRequestFromIds(targetUserId, requestingUserId));
    }

    @GetMapping("/isRequested")
    public boolean isRequested(@RequestParam("requestingUserId") String requestingUserId,@RequestParam("targetUserId") String targetUserId){
        return friendService.isRequested(requestingUserId, targetUserId);
    }

    @GetMapping("/isPending")
    public boolean isPending(@RequestParam("requestingUserId") String requestingUserId,@RequestParam("targetUserId") String targetUserId){
        return friendService.isPending(requestingUserId, targetUserId);
    }

    @PostMapping("/acceptRequest")
    public ResponseEntity<UserProfile> acceptRequest(@RequestBody FriendRequest friendRequest){
        try {
            return ResponseEntity.ok(friendService.acceptRequest(friendRequest));
        }catch(IllegalStateException | UserProfileNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    }

}
