package com.rafaelhosaka.shareme.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "post")
public class Post extends  BasePost{
    private String fileName; //S3 key
    private String fileType;
    private Set<String> sharedUsersId = new HashSet<>();
}
