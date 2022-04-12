package com.rafaelhosaka.shareme.post;

import com.rafaelhosaka.shareme.post.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, Long> {
}
