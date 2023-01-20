package com.project.matchingsystem.dto;

import com.project.matchingsystem.domain.User;
import lombok.Getter;

@Getter
public class UserProfileResponseDto {
    private String nickname;
    private String imagePath;
    private String content;

    public UserProfileResponseDto(User user) {
        this.nickname = user.getNickname();
        this.imagePath = user.getImagePath();
        this.content = user.getContent();
    }
}
