package com.example.repository;

import com.example.entity.Message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    
    // Deletes a message by its ID using a custom JPQL query.
    // Returns the number of rows affected (should be 1 if successful, 0 if not found).
    @Modifying
    @Query("DELETE FROM Message m WHERE m.id = :messageId")
    int deleteMessageById(@Param("messageId") Integer messageId);  

    // Updates the text of a message by its ID using a custom JPQL query.
    // Returns the number of rows affected (should be 1 if successful, 0 if not found).
    @Modifying
    @Query("UPDATE Message m SET m.messageText = :messageText WHERE m.id = :messageId")
    int updateMessageText(@Param("messageId") Integer messageId, @Param("messageText") String messageText);

    // Retrieves all messages posted by a specific account, identified by accountId.
    List<Message> findByPostedBy(Integer accountId);
}
