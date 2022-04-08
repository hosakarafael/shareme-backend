package com.rafaelhosaka.shareme.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository =  userAccountRepository;
    }

    public void save(UserAccount userAccount) {
        userAccountRepository.save(userAccount);
    }

    public List<UserAccount> getAll() {
        return userAccountRepository.findAll();
    }
}
