package com.rafaelhosaka.shareme.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<Post> getAll() {
        return postService.getAll();
    }

    @GetMapping("{id}")
    public Post getPostById(@PathVariable("id") String id) {
        return postService.getPostById(id);
    }

    @PostMapping
    public void save(@RequestBody Post post) {
        postService.save(post);
    }

    @PostMapping(
            path = "/upload",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public void savePostWithImage(@RequestPart("post") String json, @RequestPart("file") MultipartFile file) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Post post = mapper.readValue(json, Post.class);
            postService.savePostWithImage(post, file);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/download/{id}")
    public byte[] downloadPostImage(@PathVariable("id") String id)  {
        return postService.downloadPostImage(id);
    }
}
