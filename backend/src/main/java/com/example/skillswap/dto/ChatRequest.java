package com.example.skillswap.dto;

import lombok.Data;

@Data
public class ChatRequest {

    private String swapSessionId;
    private String receiverId;
    private String message;
}