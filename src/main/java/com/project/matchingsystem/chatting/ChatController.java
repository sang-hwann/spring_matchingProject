package com.project.matchingsystem.chatting;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")  // 여기는 컨피그에서 막아둬야함.
public class ChatController {

    private final ChatService chatService;

    // 방을 만들 떄, 원하는 셀러 닉네임을 같이 보내서,
    @PostMapping
    public ChatRoom createRoom(@RequestParam String name) {
        return chatService.createRoom(name);
    }

    @GetMapping
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }
}
