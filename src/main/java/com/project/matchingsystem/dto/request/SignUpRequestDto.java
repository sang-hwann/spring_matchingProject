package com.project.matchingsystem.dto.request;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class SignUpRequestDto {

    @Size(min = 4, max = 10, message = "username : 최소 4자 이상 10자 이하여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "username : 알파벳 소문자와 숫자로만 이루어져야 합니다.")
    private String username;

    @Size(min = 8, max = 15, message = "password : 최소 8자 이상 15자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]*$", message = "password : 알파벳 대소문자, 숫자, 특수문자(@$!%*?&)가 최소 1개 이상 포함되어야 합니다.")
    private String password;

    @NotBlank
    @Size(min = 2, max = 10)
    private String nickname;

    @Value("${profile.default.image.path}")
    private String imagePath;

}
