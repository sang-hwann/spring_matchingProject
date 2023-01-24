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
        현재는 채팅방 개설하고 채팅방 아이디만 있으면 몇 명이든 채팅이 가능한 구조.
        또한 메세지를 보낼 때, 본인의 닉네임도 같이 보내고 있음

        1. 유저 일 때만 채팅방 개설 가능하도록 만들기 -> SecurityConfig 에 추가하기
        2. 메세지에 시간 추가하기 (x)
        3. ChatMessageDTO 분리하기 Request , Response << 근데 디비에 저장이 되질 않으니까 너무 헷갈림.
        3-1. RequestChatMessage 필드에는 sender 삭제하고 ResponseChatMessage 필드에 추가하기
        3-2 sender 는 유저 닉네임을 받도록 하기
        4. 유저가 채팅방을 개설할 때, 대화할 상대인 셀러를 지칭해서 방을 개설하고 채팅방이 만들어지면 셀러가 해당 채팅방의 키값을 갖고 있도록 하기
            4-1. 여기서 생각해 볼 것. 채팅방 정보(채팅방 제목, 채팅방 아이디)를 저장할 테이블을 만들고 각 유저는 자신의 채팅방을 갖고 있어야 하지 않을까?
                4-1-1. 근데 어떻게 유저 2명을 칼럼으로 갖게 할 수 있을까? 쿼리로...? 애초에 샐러랑 유저랑 분리 해뒀다면 ...가능했을텐데...
                    4-1-1-1. 그냥 필드값으로 유저 네임, 셀러 네임 이렇게 두 개를 만들어둘까??
                4-1-2. 유저 api 에 채팅방 조회 메서드를 만들어서 본인의 채팅방(채팅방 아이디)를 조회하고 이를 통해 채팅방에 참여할 수 있도록 하는 게 어떨까?
        5. 추가사항 ) 채팅방 삭제는 어떻게 할 것 인가.


        6. 지금 가장 ㅋㅋㅋ어이 없는 코드는 ChatRoom handleActions 임. 방법을 몰라서 그냥 이넘으로 구분 시켜버렸다...  
     */


    private final ChatService chatService;

    @PostMapping
    public ChatRoom createRoom(@RequestParam(value = "room_name") String name, @RequestParam(value = "seller") String sellerName, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.createRoom(name,userDetails.getUser(),sellerName);
    }

    @GetMapping
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }
}
