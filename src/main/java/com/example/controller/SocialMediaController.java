package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */


@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private MessageService messageService;

    // Handles user registration requests by accepting an Account object,
    // tries to register it via the AccountService, and returns appropriate HTTP responses.
    // Returns 200 OK with the saved account if successful,
    // 400 BAD REQUEST if input is invalid,
    // or 409 CONFLICT if username already exists.
    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody Account account) {
        try {
            Account savedAccount = accountService.register(account);
            return ResponseEntity.ok(savedAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            if (e.getMessage().equals("Username already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Handles user login requests by accepting Account credentials (username and password),
    // delegates authentication to AccountService, and returns the Account if successful.
    @PostMapping("/login")
    public Account login(@RequestBody Account account) {
        return accountService.login(account.getUsername(), account.getPassword());
    }


    /// Creates a new message from the provided Message object,
    // calls MessageService to persist it, and returns 200 OK with the created message.
    // Returns 400 BAD REQUEST if message validation fails.
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        try {
            Message createdMessage = messageService.createMessage(message);
            return new ResponseEntity<>(createdMessage, HttpStatus.OK);  // 200 OK if successful
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // 400 if validation fails
        }
    }
    // Retrieves and returns a list of all messages.
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    // Retrieves a message by its ID.
    // Returns 200 OK with the message if found,
    // or 200 OK with null body if no message matches the ID (could be improved to 404).
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return ResponseEntity.ok().body(null);  // Return 404 if no message found
        }
        return ResponseEntity.ok(message);  // Return 200 with the message
    }


    // Deletes a message identified by its ID.
    // Returns 200 OK with "1" if deletion was successful,
    // or 200 OK with an empty body if no message was found to delete.
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable Integer messageId) {
        int deletedRows = messageService.deleteMessageById(messageId);
        if (deletedRows == 1) {
            return ResponseEntity.ok("1");  // Message deleted, return 1 to indicate successful deletion
        }
        return ResponseEntity.ok("");  // No message found, return an empty body
    }


    // Updates the text of a message identified by its ID using the provided new text in request body.
    // Returns 200 OK with "1" if update was successful,
    // or 400 BAD REQUEST with empty body if update failed.
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<String> updateMessage(
            @PathVariable Integer messageId,
            @RequestBody Map<String, String> requestBody) {
        String newText = requestBody.get("messageText"); 
        int updatedRows = messageService.updateMessageTextById(messageId, newText);
        if (updatedRows == 1) {
            return ResponseEntity.ok("1");
        } else {
            return ResponseEntity.badRequest().body("");
        }
    }

    // Retrieves all messages created by a specific account identified by accountId.
    // Returns 200 OK with the list of messages.
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.ok(messages); 
    }


}
