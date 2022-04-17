package com.rafaelhosaka.shareme.applicationuser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser {
    @Id
    private String id;
    private String username;
    private String password;

    @DBRef
    private List<Role> roles = new ArrayList<>();
}
