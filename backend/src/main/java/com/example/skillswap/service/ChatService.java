package com.example.skillswap.service;

import java.util.List;

import com.example.skillswap.model.ChatMessage;
import com.example.skillswap.model.ChatRoom;

public interface ChatService {

    ChatRoom createRoom(String swapSessionId, String userAId, String userBId);

    ChatRoom getRoomBySession(String swapSessionId);

    ChatMessage sendMessage(ChatMessage message, String loggedUserId);

    List<ChatMessage> getMessages(String roomId);
    
    List<ChatRoom> getRoomsByUserId(String userId);
    ChatMessage saveMessage(ChatMessage msg);
    
    boolean deleteMessage(String chatId, String loggedUserId);
}