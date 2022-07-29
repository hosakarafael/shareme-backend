package com.rafaelhosaka.shareme.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rafaelhosaka.shareme.comment.Comment;
import com.rafaelhosaka.shareme.like.Like;
import com.rafaelhosaka.shareme.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Post {

    @Id
    private String id;

    private String description;

    private LocalDateTime dateCreated;

    private String fileName; //S3 key

    private Set<Like> likes = new HashSet<>();

    @DBRef
    private UserProfile user;

    @DBRef
    private List<Comment> comments = new ArrayList<>();

    public Post(String description, LocalDateTime dateCreated, String fileName) {
        this.description = description;
        this.dateCreated = dateCreated;
        this.fileName = fileName;
    }
}
