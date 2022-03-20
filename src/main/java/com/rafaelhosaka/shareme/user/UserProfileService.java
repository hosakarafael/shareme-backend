package com.rafaelhosaka.shareme.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void save(UserProfile userProfile) {
        userRepository.save(userProfile);
    }
}
