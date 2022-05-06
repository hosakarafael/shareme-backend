package com.rafaelhosaka.shareme.post;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private List<Like> likes = new ArrayList<>();

    @DBRef
    private UserProfile user;

    public int getLikeCount(){
        return likes.size();
    }

    public Post(String description, LocalDateTime dateCreated, String fileName) {
        this.description = description;
        this.dateCreated = dateCreated;
        this.fileName = fileName;
    }
}
