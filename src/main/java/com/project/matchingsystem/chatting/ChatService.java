package com.project.matchingsystem.chatting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.matchingsystem.domain.Chatting;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.repository.ChattingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, Chatting> chattingMap;
    private final ChattingRepository chattingRepository;

    @PostConstruct
    private void init() {
        chattingMap = new LinkedHashMap<>();
    }

    public List<ChattingResponseDto> findMyRoom(String nickName) {
        ArrayList<ChattingResponseDto> list = new ArrayList<>();
        for (Chatting chatting : chattingMap.values()) {
            if (chatting.getUserName().equals(nickName)||chatting.getSellerName().equals(nickName)) {
                list.add(new ChattingResponseDto(chatting));
            }
        }
        return list;
    }

    public Chatting findRoomById(String roomId) {
        return chattingMap.get(roomId);
    }

    @Transactional
    public ChattingResponseDto createRoom(String nickname, String sellerName) {
        if (chattingRepository.existsByUserNameAndSellerName(nickname, sellerName)) {
            throw new IllegalArgumentException(ErrorCode.DUPLICATED_CHATTING.getMessage());
        }

        String randomId = UUID.randomUUID().toString();
        Chatting chatting = Chatting.builder()
                .roomId(randomId)
                .userName(nickname)
                .sellerName(sellerName)
                .build();
        chattingMap.put(randomId, chatting);
        chattingRepository.save(new Chatting(randomId,nickname,sellerName));
        return new ChattingResponseDto(chatting);
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
