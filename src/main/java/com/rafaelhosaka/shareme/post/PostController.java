package com.rafaelhosaka.shareme.post;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public List<Post> getAll() {
        return postService.getAll();
    }

    @PostMapping("/")
    public void save(@RequestBody Post post) {
        postService.save(post);
    }
}
