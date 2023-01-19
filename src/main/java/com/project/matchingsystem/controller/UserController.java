package com.project.matchingsystem.controller;

import com.project.matchingsystem.dto.*;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.jwt.JwtProvider;
import com.project.matchingsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @PostMapping("/sign-up")
    public ResponseStatusDto signUp(@Validated @RequestBody SignUpRequestDto signUpRequestDto) {
        return userService.signUp(signUpRequestDto);
    }
    @PostMapping("/sign-up/admin")
    public ResponseStatusDto signUp(@Validated @RequestBody SignUpAdminRequestDto signUpAdminRequestDto) {
        if (!signUpAdminRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_AUTH_TOKEN.getMessage());
        }
        return userService.signUpAdmin(signUpAdminRequestDto);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseStatusDto> signIn(@Validated @RequestBody SignInRequestDto signInRequestDto) {
        TokenResponseDto tokenResponse = userService.signIn(signInRequestDto);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(JwtProvider.ACCESSTOKEN_HEADER, tokenResponse.getAccessToken());
        responseHeaders.add(JwtProvider.REFRESHTOKEN_HEADER, tokenResponse.getRefreshToken());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new ResponseStatusDto(HttpStatus.OK.toString(), "로그인 완료"));
    }

    public ResponseStatusDto signOut() {
        return null;
    }

    public ResponseStatusDto applySellerRole() {
        return null;
    }

}
