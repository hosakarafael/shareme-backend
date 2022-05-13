package com.rafaelhosaka.shareme.user;

import com.rafaelhosaka.shareme.bucket.BucketName;
import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import com.rafaelhosaka.shareme.filestore.FileStore;
import com.rafaelhosaka.shareme.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public UserProfile save(UserProfile userProfile) {

        if(userRepository.findUserProfileByEmail(userProfile.getEmail()).isPresent()){
            throw new IllegalStateException("This email is already registered");
        }

        if(!Validator.isValidDate(userProfile.getBirthDate())){
            throw new IllegalStateException("Invalid date");
        }
        if(userProfile.getEmail().isEmpty()){
            throw new IllegalStateException("Email cannot be empty");
        }
        if(userProfile.getFirstName().isEmpty()){
            throw new IllegalStateException("First name cannot be empty");
        }
        if(userProfile.getLastName().isEmpty()){
            throw new IllegalStateException("Last name cannot be empty");
        }
        if(userProfile.getGender().getName().equals("")){
            throw new IllegalStateException("Gender cannot be empty");
        }

        return userRepository.save(userProfile);

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
