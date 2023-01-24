package com.project.matchingsystem.chatting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.matchingsystem.domain.Chatting;
import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.enums.UserRoleEnum;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.repository.ChattingRepository;
import com.project.matchingsystem.repository.UserRepository;
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
    private Map<String, ChatRoom> chatRooms;

    private final UserRepository userRepository;
    private final ChattingRepository chattingRepository;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    @Transactional
    public ChatRoom createRoom(String name, User user, String sellerName) {
        // 1. 이미 해당 셀러와 개설된 방이 있는지를 확인해야 한다.
        if(chattingRepository.existsByUserNameAndSellerName(user.getUsername(), sellerName)){
            new IllegalArgumentException(ErrorCode.DUPLICATED_CHATTING.getMessage());
        }
        // 2. sellerName 으로 유저를 찾아서 해당 유저가 셀러인지 확인해야 한다.
        if(userRepository.findByNickname(sellerName).get().getUserRole().equals(UserRoleEnum.SELLER)){
            String randomId = UUID.randomUUID().toString();
            ChatRoom chatRoom = ChatRoom.builder()
                    .roomId(randomId)
                    .name(name)
                    .userName(user.getUsername())
                    .sellerName(sellerName)
                    .build();
            chatRooms.put(randomId, chatRoom);
            return chatRoom;
        }else throw new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()); // 에러 메시지 이걸로 해도 되나? 적당한 걸로 해둔건데
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}