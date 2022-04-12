package com.rafaelhosaka.shareme.post;

import com.rafaelhosaka.shareme.post.Post;
import com.rafaelhosaka.shareme.post.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public void save(Post post) {
        post.setDateCreated(LocalDateTime.now());
        postRepository.save(post);
    }
}
