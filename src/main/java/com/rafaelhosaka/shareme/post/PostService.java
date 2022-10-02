package com.rafaelhosaka.shareme.post;


import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.rafaelhosaka.shareme.bucket.BucketName;
import com.rafaelhosaka.shareme.comment.Comment;
import com.rafaelhosaka.shareme.comment.CommentService;
import com.rafaelhosaka.shareme.exception.CommentNotFoundException;
import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import com.rafaelhosaka.shareme.filestore.FileStore;

import com.rafaelhosaka.shareme.user.UserProfile;
import com.rafaelhosaka.shareme.user.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final UserProfileRepository userRepository;
    private final FileStore fileStore;

    @Autowired
    public PostService(PostRepository postRepository, CommentService commentService, FileStore fileStore, UserProfileRepository userRepository) {
        this.postRepository = postRepository;
        this.commentService = commentService;
        this.fileStore = fileStore;
        this.userRepository = userRepository;
    }

    public List<BasePost> getAll() {
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
            post.setFileType(file.getContentType());
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

    public List<String> downloadPostImage(String postId) throws PostNotFoundException {
        Post post = (Post) getPostById(postId);
        List returnData = new ArrayList();
        try{
            S3Object object =  fileStore.download(
                    String.format("%s/%s", BucketName.POSTS.getName(), postId) ,
                    post.getFileName());
            byte[] encoded = object == null ? new byte[0] : Base64.getEncoder().encode(IOUtils.toByteArray(object.getObjectContent()));
            returnData.add(new String(encoded, StandardCharsets.US_ASCII));
            returnData.add(post.getFileType());
            return returnData;
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }



    public BasePost getPostById(String id) throws PostNotFoundException {
        return postRepository.findById(id).orElseThrow(
                () ->  new PostNotFoundException("Post with ID "+id+" not found")
        );
    }

    public List<BasePost> getPostsByUsers(List<String> usersIds) {
        List<BasePost> posts = new ArrayList<>();
        for (String id: usersIds) {
            posts.addAll(postRepository.getPostByUserId(id));
        }
        return posts;
    }

    public void deletePost(String postId) throws PostNotFoundException, CommentNotFoundException {
        BasePost post = getPostById(postId);

        if(post instanceof Post) {
            if (((Post) post).getFileName() != null && !((Post)post).getFileName().isEmpty()) {
                fileStore.delete(String.format("%s/%s", BucketName.POSTS.getName(), post.getId()), ((Post)post).getFileName());
            }
        }else{
            Post sharedPost = (Post) getPostById(((SharedPost)post).getSharedPost().getId());
            Set<String> newSharedUserId = sharedPost.getSharedUsersId().stream().filter(id ->  !((SharedPost)post).getSharedPost().getUser().getId().equals(id)).collect(Collectors.toSet());
            sharedPost.setSharedUsersId(newSharedUserId);
            postRepository.save(sharedPost);
        }

        for(Comment comment : post.getComments()){
            commentService.deleteComment(comment.getId(),post.getId());
        }

        postRepository.delete(post);
    }

    public List<BasePost> sharePost(String sharedPostId, String sharingUserId) throws  PostNotFoundException, UserProfileNotFoundException {
        Post post = (Post) postRepository.findById(sharedPostId).orElseThrow(
                () -> new PostNotFoundException("Post with ID "+sharedPostId+" not found")
        );

        UserProfile user = userRepository.findById(sharingUserId).orElseThrow(
                () -> new UsernameNotFoundException("User with ID "+sharingUserId+" not found")
        );

        List<BasePost> result = new ArrayList<>();

        SharedPost sharedPost = new SharedPost();
        sharedPost.setDateCreated(LocalDateTime.now());
        sharedPost.setUser(user);
        sharedPost.setSharedPost(post);
        result.add(postRepository.save(sharedPost));

        post.getSharedUsersId().add(sharingUserId);
        result.add(postRepository.save(post));

        return result;
    }

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }
}
