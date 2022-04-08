package com.rafaelhosaka.shareme.account;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class UserAccount {
    @Id
    private String email;

    private String password;
}
