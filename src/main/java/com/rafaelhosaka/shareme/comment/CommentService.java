package com.rafaelhosaka.shareme.comment;

import com.rafaelhosaka.shareme.exception.CommentNotFoundException;
import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.post.Post;
import com.rafaelhosaka.shareme.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CommentService {
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    @Autowired
    public CommentService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Comment newComment(Comment comment, String postId) throws PostNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(("Post with ID "+postId+" not found"))
        );

        comment.setDateCreated(LocalDateTime.now());
        comment.setSubComments(new ArrayList<>());
        comment = commentRepository.save(comment);
        post.getComments().add(comment);
        postRepository.save(post);

        return comment;
    }

    public Comment replyComment(Comment comment, String parentCommentId) throws  CommentNotFoundException{
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(
                () -> new CommentNotFoundException(("Comment with ID "+parentCommentId+" not found"))
        );

        comment.setDateCreated(LocalDateTime.now());
        comment = commentRepository.save(comment);
        parentComment.getSubComments().add(comment);
        parentComment = commentRepository.save(parentComment);

        return parentComment;
    }
}
