package com.sparta.controller;

import com.sparta.dto.ResponseStatusDto;
import com.sparta.dto.SignInRequestDto;
import com.sparta.dto.SignUpRequestDto;
import com.sparta.service.UserService;
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
