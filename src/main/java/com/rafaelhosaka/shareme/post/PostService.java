package com.rafaelhosaka.shareme.post;


import com.rafaelhosaka.shareme.bucket.BucketName;
import com.rafaelhosaka.shareme.filestore.FileStore;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileStore fileStore;

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public Post save(Post post) {
        post.setDateCreated(LocalDateTime.now());
        return postRepository.save(post);
    }

    public void savePostWithImage(Post post, MultipartFile file) {
        try {

            String fileName =  String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
            post.setFileName(fileName);
            post.setDateCreated(LocalDateTime.now());
            post = save(post);

            fileStore.upload(
                    String.format("%s/%s", BucketName.PROFILE_IMAGE.getName(), post.getId()),
                    fileName,
                    Optional.of(fileStore.getMetadata(file)),
                    file.getInputStream());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public byte[] downloadPostImage(String postId)   {
        Post post = getPostById(postId);
        return fileStore.download(
                String.format("%s/%s", BucketName.PROFILE_IMAGE.getName(), postId) ,
                post.getFileName());
    }



    public Post getPostById(String id) {
        return postRepository.findById(id).get();
    }
}
