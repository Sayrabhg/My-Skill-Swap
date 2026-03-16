package com.example.skillswap.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "chat_rooms")
public class ChatRoom {

    @Id
    private String id;

    private String userAId;
    private String userBId;

    private String swapSessionId;

}