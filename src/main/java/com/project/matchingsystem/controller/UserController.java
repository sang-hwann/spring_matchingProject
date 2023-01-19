package com.project.matchingsystem.controller;

import com.project.matchingsystem.dto.*;
import com.project.matchingsystem.jwt.JwtProvider;
import com.project.matchingsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseStatusDto signUp(@Validated @RequestBody SignUpRequestDto signUpRequestDto) {
        return userService.signUp(signUpRequestDto);
    }

    @PostMapping("/sign-in")
    public ResponseStatusDto signIn(@Validated @RequestBody SignInRequestDto signInRequestDto, HttpServletResponse response) {
        TokenResponseDto tokenResponse = userService.signIn(signInRequestDto);
        response.addHeader(JwtProvider.ACCESSTOKEN_HEADER, tokenResponse.getAccessToken());
        response.addHeader(JwtProvider.REFRESHTOKEN_HEADER, tokenResponse.getRefreshToken());
        return new ResponseStatusDto(HttpStatus.OK.toString(), "로그인 성공");
    }

    public ResponseStatusDto signOut() {
        return null;
    }

    public ResponseStatusDto applySellerRole() {
        return null;
    }





    //sh
    @PostMapping("/seller-apply/{sellerManagementId}")
    public ResponseStatusDto seller_request(@PathVariable Long sellerManagementId) {
        return userService.sellerRequest(sellerManagementId);
    }


}
