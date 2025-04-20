package com.example.chatapp.controller;

import com.example.chatapp.entity.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public Message sendMessage(Message message) {
        return message;
    }
}