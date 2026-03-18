package com.example.skillswap.dto;

import lombok.Data;

@Data
public class TypingMessage {
    private String roomId;
    private String userId;
    private boolean typing;
}