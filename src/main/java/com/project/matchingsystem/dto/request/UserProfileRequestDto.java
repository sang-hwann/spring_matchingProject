package com.project.matchingsystem.dto.request;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Size;

@Getter
public class UserProfileRequestDto {

    @Size(min = 2, max = 10)
    private String nickname;

    @Size(min = 2, max = 500)
    private String content;

    @Value("${profile.default.image.path}")
    private String imagePath;

}
