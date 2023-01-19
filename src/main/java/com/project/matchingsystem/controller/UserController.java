package com.project.matchingsystem.controller;

import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.dto.*;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.jwt.JwtProvider;
import com.project.matchingsystem.repository.UserRepository;
import com.project.matchingsystem.security.UserDetailsImpl;
import com.project.matchingsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

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

    @GetMapping("/users/{userId}/profile")
    public UserProfileResponseDto getUserProfile(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );
        return userService.getUserProfile(userId);
    }

    @PostMapping("/user/profile")
    public UserProfileResponseDto updateUserProfile(@RequestBody UserProfileRequestDto userProfileRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateUserProfile(userProfileRequestDto, userDetails.getUsername());
    }

}
