package com.rafaelhosaka.shareme.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.user.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/post")
@Slf4j
public class PostController {

    private final PostService postService;
    private final UserProfileService userProfileService;

    @Autowired
    public PostController(PostService postService, UserProfileService userProfileService) {
        this.postService = postService;
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public List<Post> getAll() {
        return postService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok().body(postService.getPostById(id));
        }catch(PostNotFoundException e){
            log.error("Exception : {}",e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(
            path = "/upload",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Post> save(@RequestPart("post") String json,@Nullable @RequestPart(value = "file", required = false) MultipartFile file) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
        try {
            Post post = mapper.readValue(json, Post.class);
            if(file != null) {
                return ResponseEntity.ok().body(postService.savePostWithImage(post, file));
            }else{
               return ResponseEntity.ok().body(postService.save(post));
            }
        } catch (JsonProcessingException e) {
            log.error("Exception : {}",e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/download/{id}")
    public byte[] downloadPostImage(@PathVariable("id") String id)  {
        try {
            return postService.downloadPostImage(id);
        }catch (PostNotFoundException e){
            log.error("Exception : {}",e.getMessage());
            return null;
        }
    }
}
