package com.rafaelhosaka.shareme.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.post.Post;
import com.rafaelhosaka.shareme.utils.JsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comment")
@Slf4j
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PutMapping("/new")
    private ResponseEntity<Comment> newComment(@RequestPart("postId") String postId, @RequestPart("comment")String commentJson){
        try {
            Comment comment = (Comment) JsonConverter.convertJsonToObject(commentJson, Comment.class);
            return ResponseEntity.ok().body(commentService.newComment(comment, postId ));
        } catch (JsonProcessingException  | PostNotFoundException e) {
            log.error("Exception : {}",e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
