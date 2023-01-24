package com.project.matchingsystem.chatting;

import com.project.matchingsystem.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")  // 여기는 컨피그에서 막아둬야함.
public class ChatController {

    /*
        현재 상태
        1. 권한을 가진 사람만 채팅룸 개설 가능, 개설시 채팅할 셀러의 이름을 명시해야함.  -> config 에서 유저만 가능하게 변경해야함. 현재는 셀러도 가능
        2. 메시지 타입을 통해 유저와 셀러 구분한다.

        추가해야 할 것
        1. 유저는 자신의 채팅방 목록을 조회할 수 있어야 한다.

        +++
        채팅방 삭제는 어떻게 하는가?
     */


    private final ChatService chatService;

    @PostMapping()
    public ChatRoom createRoom(@RequestParam(value = "room_name") String name, @RequestParam(value = "seller") String sellerName, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.createRoom(name,userDetails.getUser(),sellerName);
    }

    @GetMapping
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }
}
