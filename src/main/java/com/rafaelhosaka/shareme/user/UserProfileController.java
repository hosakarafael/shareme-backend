package com.rafaelhosaka.shareme.user;

import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@Slf4j
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

    @GetMapping("/{email:.+}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable("email") String email){
        try {
            return ResponseEntity.ok().body(userService.getUserProfileByEmail(email));
        }catch (UserProfileNotFoundException e){
            log.error("Exception : {}",e.getMessage());
            return new ResponseEntity(
                    e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public void saveUserProfile(@RequestBody UserProfile userProfile){
        userService.save(userProfile);
    }

}
