package com.project.matchingsystem.controller;

import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.dto.SignInRequestDto;
import com.project.matchingsystem.dto.SignUpRequestDto;
import com.project.matchingsystem.dto.TokenResponseDto;
import com.project.matchingsystem.jwt.JwtProvider;
import com.project.matchingsystem.repository.UserRepository;
import com.project.matchingsystem.security.UserDetailsImpl;
import com.project.matchingsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
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

    //sh
    @PostMapping("/seller-apply/{sellerManagementId}")
    public ResponseStatusDto sellerRequest(@PathVariable Long sellerManagementId) {
        return userService.sellerRequest(sellerManagementId);
    }

}
