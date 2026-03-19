package com.example.skillswap.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.PrePersist; // ✅ IMPORTANT

@Data
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;

    private String roomId;
    private String senderId;
    private String message;

    private LocalDate date;
    private String time;

    // ✅ AUTO SET DATE + TIME BEFORE SAVE
    @PrePersist
    public void onCreate() {
        this.date = LocalDate.now();
        this.time = LocalTime.now()
                .format(DateTimeFormatter.ofPattern("hh:mm a")); // 12-hour format
    }
}