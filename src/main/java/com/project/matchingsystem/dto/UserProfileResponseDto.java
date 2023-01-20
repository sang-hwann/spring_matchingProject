package com.project.matchingsystem.dto;

import com.project.matchingsystem.domain.User;
import lombok.Getter;

@Getter
public class UserProfileResponseDto {
    private String nickname;
    private byte[] image;
    private String content;
    public UserProfileResponseDto(User user) {
        this.nickname = user.getNickname();
        this.image = user.getImage();
        this.content = user.getContent();
    }
}
