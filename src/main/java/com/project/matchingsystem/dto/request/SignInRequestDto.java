package com.project.matchingsystem.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SignInRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
