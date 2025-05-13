package com.example.service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message message) {
        // Validate message text
        if (message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message text must be between 1 and 255 characters.");
        }

        // Check if the account exists based on postedBy (accountId)
        Account account = accountRepository.findById(message.getPostedBy()).orElse(null);
        if (account == null) {
            throw new IllegalArgumentException("Posted by user does not exist.");
        }

        // Set the time posted (epoch time)
        if (message.getTimePostedEpoch() == null) {
            message.setTimePostedEpoch(System.currentTimeMillis() / 1000);  // current epoch time in seconds
        }

        // Save the message to the database
        return messageRepository.save(message);
    }


    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId) {
        return messageRepository.findById(messageId).orElse(null);  // Return null if no message found
    }
    
}
