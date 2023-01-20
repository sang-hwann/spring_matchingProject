package com.project.matchingsystem.dto;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
public class UserProfileRequestDto {

    @Min(2) @Max(10)
    private String nickname;

    @Min(10) @Max(500)
    private String content;

    private String imagePath;
}
