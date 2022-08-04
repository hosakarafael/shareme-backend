package com.rafaelhosaka.shareme.share;

import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import com.rafaelhosaka.shareme.post.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/share")
@Slf4j
public class ShareController {
    private ShareService shareService;

    @Autowired
    public ShareController(ShareService shareService){
        this.shareService = shareService;
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<List<SharedPost>> getSharedPostById(@PathVariable("id") String userId){
        return ResponseEntity.ok().body(shareService.getSharedPostByUserId(userId));
    }

    @PostMapping("/post")
    public ResponseEntity<Post> sharePost(@RequestPart("sharingUserId")String sharingUserId, @RequestPart("sharingPostId")String sharingPostId){
        try {
            return ResponseEntity.ok().body(shareService.sharePost(sharingPostId, sharingUserId));
        } catch (PostNotFoundException | UserProfileNotFoundException e) {
            log.error("Exception : {}",e.getMessage());
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/post/getPostsByUsersId")
    public ResponseEntity<List<SharedPost>> getPostsByUsersId(@RequestBody List<String> usersIds){
        return ResponseEntity.ok(shareService.getPostsByUsers(usersIds));
    }
}
