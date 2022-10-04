package com.rafaelhosaka.shareme.group;

import com.rafaelhosaka.shareme.bucket.BucketName;
import com.rafaelhosaka.shareme.filestore.FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            group.setFileName(fileName);
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
}
