package com.elimishamkulima.model;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    String chatMessage;
    String chatSender;
    String chatTime;

    public ChatMessage() {
    }

    public ChatMessage(String chatMessage, String chatSender, String chatTime) {
        this.chatMessage = chatMessage;
        this.chatSender = chatSender;
        this.chatTime = chatTime;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getChatSender() {
        return chatSender;
    }

    public void setChatSender(String chatSender) {
        this.chatSender = chatSender;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }


}
