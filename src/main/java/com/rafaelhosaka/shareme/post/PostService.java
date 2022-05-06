package com.rafaelhosaka.shareme.post;


import com.rafaelhosaka.shareme.bucket.BucketName;
import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import com.rafaelhosaka.shareme.filestore.FileStore;

import com.rafaelhosaka.shareme.like.Like;
import com.rafaelhosaka.shareme.user.UserProfile;
import com.rafaelhosaka.shareme.user.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.util.ArrayUtils;


import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final FileStore fileStore;

    @Autowired
    public PostService(PostRepository postRepository, FileStore fileStore) {
        this.postRepository = postRepository;
        this.fileStore = fileStore;
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public Post save(Post post) {
        post.setDateCreated(LocalDateTime.now());
        return postRepository.save(post);
    }

    public Post savePostWithImage(Post post, MultipartFile file) {
        try {

            String fileName =  String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
            post.setFileName(fileName);
            post.setDateCreated(LocalDateTime.now());
            post = save(post);

            fileStore.upload(
                    String.format("%s/%s", BucketName.POSTS.getName(), post.getId()),
                    fileName,
                    Optional.of(fileStore.getMetadata(file)),
                    file.getInputStream());

        }catch (Exception e){
            e.printStackTrace();
        }
        return post;
    }

    public byte[] downloadPostImage(String postId) throws PostNotFoundException {
        Post post = getPostById(postId);
        return fileStore.download(
                String.format("%s/%s", BucketName.POSTS.getName(), postId) ,
                post.getFileName());
    }



    public Post getPostById(String id) throws PostNotFoundException {
        return postRepository.findById(id).orElseThrow(
                () ->  new PostNotFoundException("Post with ID "+id+" not found")
        );
    }

    public Post toggleLike(String userId, String postId) throws PostNotFoundException {
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
