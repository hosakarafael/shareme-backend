package com.rafaelhosaka.shareme.share;

import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import com.rafaelhosaka.shareme.post.Post;
import com.rafaelhosaka.shareme.post.PostRepository;
import com.rafaelhosaka.shareme.user.UserProfile;
import com.rafaelhosaka.shareme.user.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShareService {
    private ShareRepository shareRepository;
    private UserProfileRepository userRepository;
    private PostRepository postRepository;

    @Autowired
    public ShareService(ShareRepository shareRepository, UserProfileRepository userRepository, PostRepository postRepository){
        this.shareRepository = shareRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    public Post sharePost(String sharedPostId, String sharingUserId) throws  PostNotFoundException, UserProfileNotFoundException {
        Post post = postRepository.findById(sharedPostId).orElseThrow(
                () -> new PostNotFoundException("Post with ID "+sharedPostId+" not found")
        );

        UserProfile user = userRepository.findById(sharingUserId).orElseThrow(
                () -> new UsernameNotFoundException("User with ID "+sharingUserId+" not found")
        );

        SharedPost sharedPost = new SharedPost();
        sharedPost.setDateCreated(LocalDateTime.now());
        sharedPost.setUser(user);
        sharedPost.setSharedPost(post);
        shareRepository.save(sharedPost);

        post.getSharedUsersId().add(sharingUserId);
        return postRepository.save(post);
    }

    public List<SharedPost> getSharedPostByUserId(String userId) {
        return shareRepository.findSharedPostByUserId(userId);
    }

    public List<SharedPost> getPostsByUsers(List<String> usersIds) {
        List<SharedPost> posts = new ArrayList<>();
        for (String id: usersIds) {
            posts.addAll(shareRepository.findSharedPostByUserId(id));
        }
        return posts;
    }
}
