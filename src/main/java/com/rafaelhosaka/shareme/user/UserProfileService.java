package com.rafaelhosaka.shareme.user;

import com.rafaelhosaka.shareme.bucket.BucketName;
import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import com.rafaelhosaka.shareme.filestore.FileStore;
import com.rafaelhosaka.shareme.post.Post;
import com.rafaelhosaka.shareme.utils.Format;
import com.rafaelhosaka.shareme.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        userProfile.setThemePreference(ThemePreference.DEVICE);

        return userRepository.save(userProfile);

    }

    public UserProfile update(UserProfile userProfile){
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

    public byte[] downloadCoverImage(String userId) throws UserProfileNotFoundException , IllegalStateException{
        UserProfile user = getUserProfileById(userId);
        return fileStore.download(
                String.format("%s/%s", BucketName.USERS.getName(), userId) ,
                user.getCoverFileName());
    }

    public UserProfile uploadCoverImage(String userId, MultipartFile file) throws UserProfileNotFoundException {
        UserProfile user = userRepository.findById(userId).orElseThrow(
                () -> new UserProfileNotFoundException("User with ID "+userId+" not found")
        );
        try {
            String fileName =  String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
            user.setCoverFileName(fileName);
            userRepository.save(user);
            upload(user,fileName, file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public UserProfile uploadProfileImage(String userId, MultipartFile file) throws UserProfileNotFoundException {
        UserProfile user = userRepository.findById(userId).orElseThrow(
                        () -> new UserProfileNotFoundException("User with ID "+userId+" not found")
                    );
        try {
            String fileName =  String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
            user.setFileName(fileName);
            userRepository.save(user);
            upload(user,fileName, file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public void upload(UserProfile user, String fileName, MultipartFile file) throws IOException {
        fileStore.upload(
                String.format("%s/%s", BucketName.USERS.getName(), user.getId()),
                fileName,
                Optional.of(fileStore.getMetadata(file)),
                file.getInputStream());
    }

    public List<UserProfile> searchUsersContainsName(String searchedName) {
        try {
            return userRepository.searchUsersContainsName(Format.escapeMetaCharacters(searchedName));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<UserProfile> getUserProfileFromIds(List<String> ids) {
        List users = new ArrayList();

        for (String id: ids) {
            Optional user = userRepository.findById(id);
            if(user.isPresent()){
                users.add(user);
            }
        }

        return users;
    }
}
