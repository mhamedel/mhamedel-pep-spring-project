package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

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
}
