package com.rafaelhosaka.shareme.comment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collation = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubComment {
    @Id
    private String id;

    private String description;

    private String userId;

    private LocalDateTime dateCreated;
}
