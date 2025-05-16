package com.example.service;

import com.example.entity.Account;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Retrieves an account by username.
    // Returns an Optional containing the Account if found, or empty if not.
    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    // Registers a new account after validating the username and password.
    // Throws IllegalArgumentException if input is invalid.
    // Throws a generic Exception if the username already exists.
    // Returns the saved Account if registration is successful.
    public Account register(Account account) throws Exception {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new Exception("Username already exists");
        }
        return accountRepository.save(account);
    }


    // Attempts to authenticate a user by matching the provided username and password.
    // Throws UnauthorizedException if the credentials are invalid.
    // Returns the Account if authentication is successful.
    public Account login(String username, String password) {
        return accountRepository.findByUsernameAndPassword(username, password)
            .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
    }
}
