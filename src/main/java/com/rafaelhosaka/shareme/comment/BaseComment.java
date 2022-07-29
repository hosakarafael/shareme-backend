package com.rafaelhosaka.shareme.comment;


import com.rafaelhosaka.shareme.like.Like;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collation = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseComment {
    @Id
    private String id;

    private String description;

    private String userId;

    private LocalDateTime dateCreated;

    private Set<Like> likes = new HashSet<>();
}
