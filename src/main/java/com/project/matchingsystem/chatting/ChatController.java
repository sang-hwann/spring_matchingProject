package com.project.matchingsystem.chatting;

import com.project.matchingsystem.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping()
    public ChatRoom createRoom(@RequestParam(value = "seller") String sellerName, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.createRoom(userDetails.getUser(),sellerName);
    }

    @GetMapping
    public List<ChatRoom> findMyRoom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.findMyRoom(userDetails.getUser().getNickname());
    }
}
