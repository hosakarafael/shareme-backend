package com.rafaelhosaka.shareme.post;


import com.rafaelhosaka.shareme.bucket.BucketName;
import com.rafaelhosaka.shareme.comment.Comment;
import com.rafaelhosaka.shareme.comment.CommentRepository;
import com.rafaelhosaka.shareme.comment.CommentService;
import com.rafaelhosaka.shareme.exception.CommentNotFoundException;
import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.filestore.FileStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final FileStore fileStore;

    @Autowired
    public PostService(PostRepository postRepository, CommentService commentService, FileStore fileStore) {
        this.postRepository = postRepository;
        this.commentService = commentService;
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

    public List<Post> getPostsByUsers(List<String> usersIds) {
        List<Post> posts = new ArrayList<>();
        for (String id: usersIds) {
            posts.addAll(postRepository.getPostByUserId(id));
        }
        return posts;
    }

    public void deletePost(String postId) throws PostNotFoundException, CommentNotFoundException {
        Post post = getPostById(postId);

        if(post.getFileName() != null && !post.getFileName().isEmpty()) {
            fileStore.delete(String.format("%s/%s", BucketName.POSTS.getName(), post.getId()), post.getFileName());
        }

        for(Comment comment : post.getComments()){
            commentService.deleteComment(comment.getId(),post.getId());
        }

        postRepository.delete(post);
    }
}
