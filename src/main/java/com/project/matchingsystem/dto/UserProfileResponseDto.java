package com.project.matchingsystem.dto;

import com.project.matchingsystem.domain.User;
import lombok.Getter;

@Getter
public class UserProfileResponseDto {

    private final String nickname;
    private final String imagePath;
    private final String content;

    public UserProfileResponseDto(User user) {
        this.nickname = user.getNickname();
        this.imagePath = user.getImagePath();
        this.content = user.getContent();
    }

}
