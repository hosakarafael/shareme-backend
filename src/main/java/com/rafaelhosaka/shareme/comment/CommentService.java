package com.rafaelhosaka.shareme.comment;

import com.rafaelhosaka.shareme.exception.CommentNotFoundException;
import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.post.BasePost;
import com.rafaelhosaka.shareme.post.Post;
import com.rafaelhosaka.shareme.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        BasePost post = postRepository.findById(postId).orElseThrow(
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
        comment.setParentId(parentCommentId);
        comment = commentRepository.save(comment);
        parentComment.getSubComments().add(comment);
        parentComment = commentRepository.save(parentComment);

        return parentComment;
    }

    public void deleteComment(String commentId, String postId) throws  CommentNotFoundException, PostNotFoundException{
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException(("Comment with ID "+commentId+" not found"))
        );

        BasePost post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(("Comment with ID "+commentId+" not found"))
        );

        if(comment.getParentId() != null) {
            Comment parentComment = commentRepository.findById(comment.getParentId()).orElseThrow(
                    () -> new CommentNotFoundException(("Comment with ID "+comment.getParentId()+" not found"))
            );

            List<BaseComment> updatedSubComments = parentComment.getSubComments().stream().filter(c -> !c.getId().equals(comment.getId())).collect(Collectors.toList());
            parentComment.setSubComments(updatedSubComments);
            commentRepository.save(parentComment);
        }else {
            for (BaseComment subComment: comment.getSubComments()) {
                commentRepository.delete((Comment)subComment);
            }
            List<Comment> updatedComments = post.getComments().stream().filter(c -> !c.getId().equals(commentId)).collect(Collectors.toList());
            post.setComments(updatedComments);
            postRepository.save(post);
        }
        commentRepository.delete(comment);
    }

    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
