package com.rafaelhosaka.shareme.like;

import com.rafaelhosaka.shareme.comment.Comment;
import com.rafaelhosaka.shareme.comment.CommentRepository;
import com.rafaelhosaka.shareme.exception.CommentNotFoundException;
import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.post.Post;
import com.rafaelhosaka.shareme.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LikeService {
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    @Autowired
    public LikeService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Post likeUnlikePost(String userId, String postId) throws PostNotFoundException {
        Post oldPost = postRepository.findById(postId).orElseThrow(
                () ->  new PostNotFoundException("Post with ID "+postId+" not found")
        );

        boolean isNewLike = true;
        Set<Like> likes = new HashSet<>();
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

    public Comment likeUnlikeComment(String userId, String commentId) throws CommentNotFoundException {
        Comment oldComment = commentRepository.findById(commentId).orElseThrow(
                () ->  new CommentNotFoundException("Comment with ID "+commentId+" not found")
        );

        boolean isNewLike = true;
        Set<Like> likes = new HashSet<>();
        for (Like like : oldComment.getLikes()) {
            if (like.getUserId().equals(userId)) {
                isNewLike = false;
            } else {
                likes.add(like);
            }
        }
        if (isNewLike){
            likes.add(new Like(userId));
        }
        oldComment.setLikes(likes);
        return commentRepository.save(oldComment);
    }
}
