package com.rafaelhosaka.shareme.user;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private UserProfileService userService;

    @Autowired
    public UserProfileController(UserProfileService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<UserProfile> getUserProfiles(){
        return userService.getUserProfiles();
    }

    @PostMapping("/")
    public void saveUserProfile(@RequestBody UserProfile userProfile){
        this.userService.save(userProfile);
    }


}
