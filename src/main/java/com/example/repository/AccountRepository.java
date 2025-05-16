package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    // Finds an account by its username.
    // Returns an Optional containing the Account if found, or empty if not.
    Optional<Account> findByUsername(String username);

    // Finds an account by both username and password (used for login).
    // Returns an Optional containing the Account if credentials match, or empty if not.
    Optional<Account> findByUsernameAndPassword(String username, String password);

}
