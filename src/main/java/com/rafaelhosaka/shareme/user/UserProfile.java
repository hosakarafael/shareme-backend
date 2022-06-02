package com.rafaelhosaka.shareme.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.rafaelhosaka.shareme.post.Post;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    @Indexed(unique = true)
    private String email;

    private LocalDate birthDate;

    private String fileName;

    private Gender gender;

    private List<String> friends = new ArrayList<>();

    public int getFriendCount(){
        return friends.size();
    }

    public String getFullName(){
            return this.firstName + " " + this.lastName;
        }
}
