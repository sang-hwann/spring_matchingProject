package com.project.matchingsystem.chatting;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private String roomId;
    private String userName;
    private String sellerName;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String userName, String sellerName) {
        this.roomId = roomId;
        this.userName = userName;
        this.sellerName = sellerName;
    }

    public void handleActions(WebSocketSession session, ChatMessageRequestDto chatMessageRequestDto, ChatService chatService) {
        if (chatMessageRequestDto.getType().equals(ChatMessageRequestDto.MessageType.ENTER_USER)) {
            sessions.add(session);
            chatMessageRequestDto.setMessage(userName + "님이 입장했습니다.");
        } else if (chatMessageRequestDto.getType().equals(ChatMessageRequestDto.MessageType.ENTER_SELLER)) {
            sessions.add(session);
            chatMessageRequestDto.setMessage(sellerName + "님이 입장했습니다.");
        } else if (chatMessageRequestDto.getType().equals(ChatMessageRequestDto.MessageType.TALK_USER)) {
            chatMessageRequestDto.setSender(userName);
        }else {
            chatMessageRequestDto.setSender(sellerName);
        }
        sendMessage(chatMessageRequestDto, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }

}
