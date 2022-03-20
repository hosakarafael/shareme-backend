package com.rafaelhosaka.shareme.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@ToString
public class UserProfile {
    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;

}
