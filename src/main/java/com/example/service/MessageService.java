package com.example.service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Creates a new message after validating message text and verifying that the associated account exists.
    // Automatically sets the current epoch time if not provided.
    // Throws IllegalArgumentException if validation fails.
    // Returns the saved Message object.
    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message text must be between 1 and 255 characters.");
        }
        Account account = accountRepository.findById(message.getPostedBy()).orElse(null);
        if (account == null) {
            throw new IllegalArgumentException("Posted by user does not exist.");
        }
        if (message.getTimePostedEpoch() == null) {
            message.setTimePostedEpoch(System.currentTimeMillis() / 1000);  
        }
        return messageRepository.save(message);
    }


    // Retrieves and returns a list of all messages from the database.
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Retrieves a message by its ID.
    // Returns the Message if found, or null if not found.
    public Message getMessageById(Integer messageId) {
        return messageRepository.findById(messageId).orElse(null); 
    }

    // Deletes a message by its ID using a custom repository query.
    // Marked as @Transactional since it performs a modifying database operation.
    // Returns 1 if deletion is successful, or 0 if the message does not exist.
    @Transactional
    public int deleteMessageById(Integer messageId) {
        return   messageRepository.deleteMessageById(messageId);
    }

    // Updates the text of a message identified by its ID after validation.
    // Returns 1 if the update is successful, or 0 if the message is invalid, not found, or update fails.
    // Marked as @Transactional since it performs a modifying database operation.
    @Transactional
    public int updateMessageTextById(Integer messageId, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return 0; 
        }
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message == null) {
            return 0; 
        }
        return messageRepository.updateMessageText(messageId, newText);
    }

    // Retrieves all messages posted by a specific account (identified by accountId).
    // Returns a list of Message objects.
    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);  
    }
    
}
