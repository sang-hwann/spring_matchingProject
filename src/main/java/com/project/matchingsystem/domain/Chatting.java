package com.project.matchingsystem.domain;

import com.project.matchingsystem.chatting.ChatMessageRequestDto;
import com.project.matchingsystem.chatting.ChatService;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Chatting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String sellerName;

    @Transient
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public Chatting(String roomId, String userName, String sellerName) {
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
        sessions.forEach(session -> chatService.sendMessage(session, message));
    }
}
