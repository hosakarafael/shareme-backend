package com.rafaelhosaka.shareme.group;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rafaelhosaka.shareme.exception.GroupNotFoundException;
import com.rafaelhosaka.shareme.user.UserProfile;
import com.rafaelhosaka.shareme.utils.JsonConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/group")
public class GroupController {
    private GroupService groupService;

    public GroupController(GroupService groupService){
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Group>> getAllGroups(){
        return ResponseEntity.ok().body(groupService.getAllGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable("id")String groupId){
        try {
            return ResponseEntity.ok().body(groupService.getGroupById(groupId));
        }catch(GroupNotFoundException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Group>> getGroupsByUserId(@PathVariable("id")String userId){
        return ResponseEntity.ok().body(groupService.getGroupsByUserId(userId));
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Group>> searchGroupsContainsName(@RequestParam("query") String searchedName) {
        return ResponseEntity.ok(groupService.searchGroupsContainsName(searchedName));
    }

    @PostMapping(path = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Group> createGroup(@RequestPart("group") String json, @RequestPart(value = "file", required = false) MultipartFile file){
        try {
            Group group = (Group) JsonConverter.convertJsonToObject(json, Group.class);
            return ResponseEntity.ok().body(groupService.createGroup(group, file));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(
            path = "/uploadCoverImage",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Group> uploadCoverImage(@RequestPart("groupId") String groupId, @RequestPart(value = "file") MultipartFile file) {
        try {
            return ResponseEntity.ok().body(groupService.uploadCoverImage(groupId, file));
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<List<String>> downloadGroupImage(@PathVariable("id") String id)  {
        try {
            return ResponseEntity.ok().body(groupService.downloadGroupImage(id));
        }catch (Exception e){
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/join")
    public ResponseEntity<Group> join(@RequestPart("groupId") String groupId, @RequestPart("userId")String userId)  {
        try {
            return ResponseEntity.ok().body(groupService.join(groupId, userId));
        }catch (Exception e){
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/leave")
    public ResponseEntity<Group> leave(@RequestPart("groupId") String groupId, @RequestPart("userId")String userId)  {
        try {
            return ResponseEntity.ok().body(groupService.leave(groupId, userId));
        }catch (Exception e){
            return ResponseEntity.noContent().build();
        }
    }
}
