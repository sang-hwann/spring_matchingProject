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
        3. 이미 같은 셀러와 채팅방이 존재한다면, 채팅방 개설 불가 -> 테스트 해봐야 함. -> 실패 ^^ ,,, 아.. 애초에 디비 저장 안했다. 헷
        4. 채팅 문제 없이 돌아감 ( 확인 완료 )

        문제
        1. 유저 측에서 메시지를 보내면 셀러도 같은 메시지를 보낸 것처럼 중복되서 출력됨, 그러나 셀러가 보낼 땐 정상 작동 -> 이유 모르겠음 ;
        2. 지금 생각해보니 한 명은 닉네임으로 잘 뜨는데 한 명은 아이디로 떴음. 아!! 닉네임이랑 아이디랑 다르구만..!! 이거 수정해야함. ㅋ.ㅋ

        추가해야 할 것
        1. 유저는 자신의 채팅방 목록을 조회할 수 있어야 한다.

        +++
        채팅방 삭제는 어떻게 하는가?

        리팩토링 할 부분
        ㅎㅎ...너무 많음...도메인 같은 ChatRoom 그리고 별 생각 없이 만든 Chatting
        서비스에 작성한 지저분한 코드 등등등


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
