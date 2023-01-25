package com.project.matchingsystem.chatting;

import com.project.matchingsystem.domain.Chatting;
import lombok.Getter;


@Getter
public class ChattingResponseDto {

    private String roomId;

    private String userName;

    private String sellerName;

    public ChattingResponseDto(Chatting chatting) {
        this.roomId = chatting.getRoomId();
        this.userName = chatting.getUserName();
        this.sellerName = chatting.getSellerName();
    }
}
