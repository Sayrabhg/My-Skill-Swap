package com.example.skillswap.listener;

import com.example.skillswap.model.ChatMessage;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class ChatMessageListener implements BeforeConvertCallback<ChatMessage> {

    @Override
    public ChatMessage onBeforeConvert(ChatMessage message, String collection) {

        // IST timezone
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

        // Set date
        message.setDate(now.toLocalDate());

        // Set 12-hour time with AM/PM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        message.setTime(now.format(formatter));

        return message;
    }
}