package com.rafaelhosaka.shareme.user;

import com.rafaelhosaka.shareme.bucket.BucketName;
import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import com.rafaelhosaka.shareme.filestore.FileStore;
import com.rafaelhosaka.shareme.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {
    private UserProfileRepository userRepository;
    private FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileRepository userRepository, FileStore fileStore) {
        this.userRepository = userRepository;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getUserProfiles(){
        return userRepository.findAll();
    }

    public UserProfile getUserProfileByEmail(String email) throws UserProfileNotFoundException {
        return userRepository.findUserProfileByEmail(email).orElseThrow(
                () ->  new UserProfileNotFoundException("User with email "+email+" not found")
        );
    }

    public UserProfile getUserProfileById(String id) throws UserProfileNotFoundException {
        return userRepository.findById(id).orElseThrow(
                () -> new UserProfileNotFoundException("User with ID "+id+" not found")
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

    public byte[] downloadProfileImage(String userId) throws UserProfileNotFoundException , IllegalStateException{
        UserProfile user = getUserProfileById(userId);
        return fileStore.download(
                String.format("%s/%s", BucketName.USERS.getName(), userId) ,
                user.getFileName());
    }
}
