package com.rafaelhosaka.shareme.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;



import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;


public class UserProfile {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
