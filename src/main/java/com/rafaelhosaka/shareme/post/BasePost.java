package com.rafaelhosaka.shareme.post;

import com.rafaelhosaka.shareme.comment.Comment;
import com.rafaelhosaka.shareme.like.Like;
import com.rafaelhosaka.shareme.user.UserProfile;
import com.rafaelhosaka.shareme.visibility.Visibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "post")
public abstract class BasePost {
    @Id
    private String id;

    private String description;

    private LocalDateTime dateCreated;

    private Set<Like> likes = new HashSet<>();

    @DBRef
    private UserProfile user;

    @DBRef
    private List<Comment> comments = new ArrayList<>();

    private Visibility visibility;
}
