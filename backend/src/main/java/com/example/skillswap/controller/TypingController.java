package com.example.skillswap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.skillswap.dto.TypingMessage;

@Controller
public class TypingController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/typing")
	public void handleTyping(TypingMessage message) {
	    messagingTemplate.convertAndSend(
	        "/topic/typing/" + message.getRoomId(),
	        message
	    );
	}
}