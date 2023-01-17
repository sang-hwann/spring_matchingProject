package com.sparta.matchingsystem.controller;

import com.sparta.matchingsystem.dto.ResponseStatusDto;
import com.sparta.matchingsystem.dto.SignInRequestDto;
import com.sparta.matchingsystem.dto.SignUpRequestDto;
import com.sparta.matchingsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;

    public ResponseStatusDto signUp(@Validated @RequestBody SignUpRequestDto signUpRequestDto) {
        return userService.signUp(signUpRequestDto);
    }

    public ResponseStatusDto signIn(@Validated @RequestBody SignInRequestDto signInRequestDto) {
        return null;
    }

    public ResponseStatusDto signOut() {
        return null;
    }

    public ResponseStatusDto applySellerRole() {
        return null;
    }

}
