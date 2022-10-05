package com.rafaelhosaka.shareme.user;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
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
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

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

    public UserProfile save(UserProfile userProfile) throws IllegalStateException {

        if(userRepository.findUserProfileByEmail(userProfile.getEmail()).isPresent()){
            throw new IllegalStateException("errorUsernameAlreadyRegistered");
        }

        if(!Validator.isValidDate(userProfile.getBirthDate())){
            throw new IllegalStateException("errorInvalidDate");
        }
        if(userProfile.getEmail() == null || userProfile.getEmail().trim().isEmpty()){
            throw new IllegalStateException("errorUsernameEmpty");
        }
        if(userProfile.getFirstName() == null || userProfile.getFirstName().trim().isEmpty()){
            throw new IllegalStateException("errorFirstNameEmpty");
        }
        if(userProfile.getLastName() == null || userProfile.getLastName().trim().isEmpty()){
            throw new IllegalStateException("errorLastNameEmpty");
        }
        if(userProfile.getGender() == null || userProfile.getGender().getName().equals("")){
            throw new IllegalStateException("errorGenderEmpty");
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

    public List<String> downloadProfileImage(String userId) throws UserProfileNotFoundException , IllegalStateException{
        UserProfile user = getUserProfileById(userId);
        List returnData = new ArrayList();

        try{
            S3Object object = fileStore.download(
                    String.format("%s/%s", BucketName.USERS.getName(), userId) ,
                    user.getFileName());
            byte[] encoded = object == null ? new byte[0] : Base64.getEncoder().encode(IOUtils.toByteArray(object.getObjectContent()));
            returnData.add(new String(encoded, StandardCharsets.US_ASCII));
            returnData.add(object == null ? "" : object.getObjectMetadata().getUserMetadata().get("content-type"));
            return returnData;
         }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    public List<String> downloadCoverImage(String userId) throws UserProfileNotFoundException , IllegalStateException{
        UserProfile user = getUserProfileById(userId);
        List returnData = new ArrayList();

        try{
         S3Object object  = fileStore.download(
                String.format("%s/%s", BucketName.USERS.getName(), userId) ,
                user.getCoverFileName());
        byte[] encoded = object == null ? new byte[0] : Base64.getEncoder().encode(IOUtils.toByteArray(object.getObjectContent()));
        returnData.add(new String(encoded, StandardCharsets.US_ASCII));
        returnData.add(object == null ? "" : object.getObjectMetadata().getUserMetadata().get("content-type"));
        return returnData;
    }catch (Exception e){
        throw new IllegalStateException(e);
    }
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

    public void changeOnlineStatusById(String id, boolean online) throws UserProfileNotFoundException {
        UserProfile user = userRepository.findById(id).orElseThrow(
                () -> new UserProfileNotFoundException("User with "+id+" not found")
        );
        user.setOnline(online);
        userRepository.save(user);
    }

    public void changeConnectionStatusById(String id, boolean connected) throws UserProfileNotFoundException {
        UserProfile user = userRepository.findById(id).orElseThrow(
                () -> new UserProfileNotFoundException("User with "+id+" not found")
        );
        user.setConnected(connected);
        userRepository.save(user);
    }

    public List<UserProfile> getUserFriends(String userId) throws UserProfileNotFoundException {
        UserProfile user = userRepository.findById(userId).orElseThrow(
                () -> new UserProfileNotFoundException("User with "+userId+" not found")
        );
        List<UserProfile> friends = new ArrayList<>();
        for (String id:user.getFriends()) {
            UserProfile friend = userRepository.findById(id).orElseThrow(
                    () -> new UserProfileNotFoundException("User with "+userId+" not found")
            );
            friends.add(friend);
        }
        return friends;
    }
}
