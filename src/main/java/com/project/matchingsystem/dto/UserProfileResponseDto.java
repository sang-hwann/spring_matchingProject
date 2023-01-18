package com.project.matchingsystem.dto;

import com.project.matchingsystem.domain.UserProfile;
import lombok.Getter;

@Getter
public class UserProfileResponseDto {
    private String nickname;
    private byte[] image;
    private String content;
    public UserProfileResponseDto(UserProfile userProfile) {
        this.nickname = userProfile.getNickname();
        this.image = userProfile.getImage();
        this.content = userProfile.getContent();
    }
}
