package com.rafaelhosaka.shareme.group;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Group>> getGroupsByUserId(@PathVariable("id")String userId){
        return ResponseEntity.ok().body(groupService.getGroupsByUserId(userId));
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
}
