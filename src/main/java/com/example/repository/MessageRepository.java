package com.example.repository;

import com.example.entity.Message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    

    @Modifying
    @Query("DELETE FROM Message m WHERE m.id = :messageId")
    int deleteMessageById(@Param("messageId") Integer messageId);  

    @Modifying
    @Query("UPDATE Message m SET m.messageText = :messageText WHERE m.id = :messageId")
    int updateMessageText(@Param("messageId") Integer messageId, @Param("messageText") String messageText);


    List<Message> findByPostedBy(Integer accountId);
}
