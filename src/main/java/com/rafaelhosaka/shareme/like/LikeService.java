package com.rafaelhosaka.shareme.like;

import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.post.Post;
import com.rafaelhosaka.shareme.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LikeService {
    private PostRepository postRepository;

    @Autowired
    public LikeService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post likePost(String userId, String postId) throws PostNotFoundException {
        Post oldPost = postRepository.findById(postId).orElseThrow(
                () ->  new PostNotFoundException("Post with ID "+postId+" not found")
        );

        boolean isNewLike = true;
        List<Like> likes = new ArrayList<>();
        for (Like like : oldPost.getLikes()) {
            if (like.getUserId().equals(userId)) {
                isNewLike = false;
            } else {
                likes.add(like);
            }
        }
        if (isNewLike){
            likes.add(new Like(userId));
        }
        oldPost.setLikes(likes);
        return postRepository.save(oldPost);
    }
}
