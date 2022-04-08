package com.rafaelhosaka.shareme.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository <UserAccount, String> {
}
