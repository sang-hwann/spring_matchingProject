package com.project.matchingsystem.chatting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.matchingsystem.domain.Chatting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSockChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);
        ChatMessageRequestDto chatMessageRequestDto = objectMapper.readValue(payload, ChatMessageRequestDto.class);
        Chatting chatting = chatService.findRoomById(chatMessageRequestDto.getRoomId());
        chatting.handleActions(session, chatMessageRequestDto, chatService);
    }
}
