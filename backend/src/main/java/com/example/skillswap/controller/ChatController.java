package com.example.skillswap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.example.skillswap.model.ChatMessage;
import com.example.skillswap.model.ChatRoom;
import com.example.skillswap.model.User;
import com.example.skillswap.service.ChatService;
import com.example.skillswap.service.UserService;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatController {

    @Autowired
    private ChatService chatService;
    
    @Autowired
    private UserService userService;

    // Create chat room
    @PostMapping("/create-room")
    public ChatRoom createRoom(@RequestBody ChatRoom room) {

        return chatService.createRoom(
                room.getSwapSessionId(),
                room.getUserAId(),
                room.getUserBId()
        );
    }

    // Get room by swap session
    @GetMapping("/room/{swapSessionId}")
    public ChatRoom getRoom(@PathVariable String swapSessionId) {

        return chatService.getRoomBySession(swapSessionId);
    }

    // Send message (secure)
    @PostMapping("/send")
    public ChatMessage sendMessage(
            @RequestBody ChatMessage message,
            Authentication authentication) {

        String email = authentication.getName(); // logged user email

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String loggedUserId = user.getId(); // MongoDB user ID

        return chatService.sendMessage(message, loggedUserId);
    }

    // Get messages
    @GetMapping("/messages/{roomId}")
    public List<ChatMessage> getMessages(@PathVariable String roomId) {

        return chatService.getMessages(roomId);
    }
    
    @DeleteMapping("/delete/{chatId}")
    public String deleteMessage(
            @PathVariable String chatId,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean deleted = chatService.deleteMessage(chatId, user.getId());

        if (deleted) {
            return "Message deleted successfully";
        }

        return "Delete failed";
    }
}