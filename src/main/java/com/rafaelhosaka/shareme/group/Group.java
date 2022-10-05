package com.rafaelhosaka.shareme.group;

import com.rafaelhosaka.shareme.post.Post;
import com.rafaelhosaka.shareme.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Group {
    @Id
    private String id;

    private Set<String> admins = new HashSet<>();

    private Set<String> members = new HashSet<>();

    private String name;

    private String fileName;

    private LocalDateTime dateCreated;
}
