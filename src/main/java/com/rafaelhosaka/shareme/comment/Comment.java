package com.rafaelhosaka.shareme.comment;

import com.rafaelhosaka.shareme.like.Like;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseComment{

    private List<BaseComment> subComments = new ArrayList<>();

}
