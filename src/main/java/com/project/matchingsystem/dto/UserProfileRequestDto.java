package com.project.matchingsystem.dto;

import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
public class UserProfileRequestDto {

    @Size(min = 2 , max = 10)
    private String nickname;

    @Size(min = 2 , max = 500)
    private String content;

    private String imagePath;

}
