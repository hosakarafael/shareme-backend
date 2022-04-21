package com.rafaelhosaka.shareme.user;

import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {
    private UserProfileRepository userRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserProfile> getUserProfiles(){
        return userRepository.findAll();
    }

    public UserProfile getUserProfileByEmail(String email) throws UserProfileNotFoundException {
        return userRepository.findUserProfileByEmail(email).orElseThrow(
                () ->  new UserProfileNotFoundException("User with email "+email+" not found")
        );
    }

    public void save(UserProfile userProfile) {
        userRepository.save(userProfile);
    }

    public UserProfile findById(String userId) throws UserProfileNotFoundException {
        return userRepository.findById(userId).orElseThrow(
                () ->  new UserProfileNotFoundException("User with ID "+userId+" not found")
        );
    }
}
