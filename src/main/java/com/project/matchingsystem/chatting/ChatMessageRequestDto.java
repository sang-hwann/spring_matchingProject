package com.project.matchingsystem.chatting;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ChatMessageRequestDto {

    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER_USER,  // 채팅방 입장
        ENTER_SELLER,
        TALK_USER, // 채팅
        TALK_SELLER
    }

    @Setter private String sender;
    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String message; // 메시지

    public void setMessage(String message) {
        this.message = message;
    }

}