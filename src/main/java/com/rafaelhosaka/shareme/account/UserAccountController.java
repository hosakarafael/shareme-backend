package com.rafaelhosaka.shareme.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("/")
    public List<UserAccount> getAll() {
        return userAccountService.getAll();
    }

    @PostMapping("/")
    public void save(@RequestBody UserAccount userAccount) {
        userAccountService.save(userAccount);
    }
}
