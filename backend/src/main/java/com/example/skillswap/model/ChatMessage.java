package com.example.skillswap.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;

    private String roomId;

    private String senderId;

    private String message;

    private LocalDateTime timestamp = LocalDateTime.now();

}