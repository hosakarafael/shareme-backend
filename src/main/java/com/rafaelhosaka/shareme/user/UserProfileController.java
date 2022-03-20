package com.rafaelhosaka.shareme.user;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-profile")
public class UserProfileController {

    private UserProfileService userService;

    @Autowired
    public UserProfileController(UserProfileService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    @ResponseBody
    public void saveUserProfile(@RequestBody UserProfile userProfile){
        this.userService.save(userProfile);
    }

    @GetMapping("/all")
    public List<UserProfile> getUserProfiles(){
        return userService.getUserProfiles();
    }
}
