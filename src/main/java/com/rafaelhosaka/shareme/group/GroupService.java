package com.rafaelhosaka.shareme.group;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.rafaelhosaka.shareme.bucket.BucketName;
import com.rafaelhosaka.shareme.exception.GroupNotFoundException;
import com.rafaelhosaka.shareme.filestore.FileStore;
import com.rafaelhosaka.shareme.post.Post;
import com.rafaelhosaka.shareme.user.UserProfile;
import com.rafaelhosaka.shareme.utils.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GroupService {
    private GroupRepository groupRepository;
    private FileStore fileStore;

    @Autowired
    public GroupService(GroupRepository groupRepository, FileStore fileStore){
        this.groupRepository = groupRepository;
        this.fileStore = fileStore;
    }

    public Group createGroup(Group group, MultipartFile file) {
        group.setDateCreated(LocalDateTime.now());
        if(file == null){
            group = groupRepository.save(group);
        }else{
            String fileName =  String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
            group.setCoverFileName(fileName);
            group = groupRepository.save(group);

            try {
                fileStore.upload(
                        String.format("%s/%s", BucketName.GROUPS.getName(), group.getId()),
                        fileName,
                        Optional.of(fileStore.getMetadata(file)),
                        file.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return group ;
    }

    public List<Group> getGroupsByUserId(String userId) {
        return groupRepository.getGroupsByUserId(userId);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public List<Group> searchGroupsContainsName(String searchedName) {
        try {
            return groupRepository.searchGroupsContainsName(Format.escapeMetaCharacters(searchedName));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<String> downloadGroupImage(String groupId) throws GroupNotFoundException {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new GroupNotFoundException("Group with id "+groupId+" not found")
        );
        List returnData = new ArrayList();
        try{
            S3Object object =  fileStore.download(
                    String.format("%s/%s", BucketName.GROUPS.getName(), groupId) ,
                    group.getCoverFileName());
            byte[] encoded = object == null ? new byte[0] : Base64.getEncoder().encode(IOUtils.toByteArray(object.getObjectContent()));
            returnData.add(new String(encoded, StandardCharsets.US_ASCII));
            returnData.add(object == null ? "" : object.getObjectMetadata().getUserMetadata().get("content-type"));
            return returnData;
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    public Group getGroupById(String groupId) throws GroupNotFoundException {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("group with id "+groupId+" not found"));
    }

    public Group uploadCoverImage(String groupId, MultipartFile file) throws GroupNotFoundException {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new GroupNotFoundException("Group with id "+groupId+" not found")
        );
        String fileName =  String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        group.setCoverFileName(fileName);
        group = groupRepository.save(group);

        try {
            fileStore.upload(
                    String.format("%s/%s", BucketName.GROUPS.getName(), group.getId()),
                    fileName,
                    Optional.of(fileStore.getMetadata(file)),
                    file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return group;
    }

    public Group join(String groupId, String userId) throws GroupNotFoundException {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new GroupNotFoundException("Group with id "+groupId+" not found")
        );
        group.getMembers().add(userId);
        return groupRepository.save(group);
    }
}
