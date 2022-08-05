package com.rafaelhosaka.shareme.like;

import com.rafaelhosaka.shareme.comment.Comment;
import com.rafaelhosaka.shareme.exception.CommentNotFoundException;
import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.post.BasePost;
import com.rafaelhosaka.shareme.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/like")
public class LikeController {

    private LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PutMapping("/post")
    public ResponseEntity<BasePost> likeUnlikePost(@RequestPart("userId") String userId, @RequestPart("postId") String postId){
        try {
            return ResponseEntity.ok().body(likeService.likeUnlikePost(userId, postId));
        }catch(PostNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/comment")
    public ResponseEntity<Comment> likeUnlikeComment(@RequestPart("userId") String userId, @RequestPart("commentId") String postId){
        try {
            return ResponseEntity.ok().body(likeService.likeUnlikeComment(userId, postId));
        }catch(CommentNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}
