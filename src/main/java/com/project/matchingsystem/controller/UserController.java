package com.project.matchingsystem.controller;

import com.project.matchingsystem.dto.request.SignInRequestDto;
import com.project.matchingsystem.dto.request.SignUpAdminRequestDto;
import com.project.matchingsystem.dto.request.SignUpRequestDto;
import com.project.matchingsystem.dto.request.UserProfileRequestDto;
import com.project.matchingsystem.dto.response.ResponseStatusDto;
import com.project.matchingsystem.dto.response.TokenResponseDto;
import com.project.matchingsystem.dto.response.UserProfileResponseDto;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.jwt.JwtProvider;
import com.project.matchingsystem.security.UserDetailsImpl;
import com.project.matchingsystem.service.UserProfileService;
import com.project.matchingsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final UserProfileService userProfileService;

    @PostMapping("/sign-up")
    public ResponseStatusDto signUp(@Validated @RequestBody SignUpRequestDto signUpRequestDto) {
        return userService.signUp(signUpRequestDto);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseStatusDto> signIn(@Validated @RequestBody SignInRequestDto signInRequestDto) {
        TokenResponseDto tokenResponse = userService.signIn(signInRequestDto);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(JwtProvider.ACCESS_TOKEN_HEADER, tokenResponse.getAccessToken());
        responseHeaders.add(JwtProvider.REFRESH_TOKEN_HEADER, tokenResponse.getRefreshToken());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new ResponseStatusDto(HttpStatus.OK.toString(), "로그인 완료"));
    }

    @GetMapping("/sign-out")
    public ResponseStatusDto signOut(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String accessToken = jwtProvider.resolveAccessToken(request);
        String username = userDetails.getUsername();
        return userService.signOut(accessToken, username);
    }

    @GetMapping("/users/{userId}/profile")
    public UserProfileResponseDto getUserProfile(@PathVariable Long userId) {
        return userService.getUserProfile(userId);
    }

    @PostMapping("/user/profile")
    public UserProfileResponseDto updateUserProfile(@Validated @RequestBody UserProfileRequestDto userProfileRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateUserProfile(userProfileRequestDto, userDetails.getUsername());
    }

    @PutMapping("/user/profile/image")
    public ResponseStatusDto uploadProfileImage(MultipartFile image, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return userProfileService.uploadUserProfileImage(image, userDetails.getUser().getId());
    }

    @GetMapping("/user/profile/image")
    public Resource downloadProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails) throws MalformedURLException {
        return userProfileService.downloadUserProfileImage(userDetails.getUser().getId());
    }

    @GetMapping("/sellers")
    public List<UserProfileResponseDto> getSellers() {
        return userService.getSellers();
    }

    @GetMapping("/sellers/{sellerId}")
    public UserProfileResponseDto getSeller(@PathVariable Long sellerId) {
        return userService.getUserProfile(sellerId);
    }

    @PostMapping("/seller-apply")
    public ResponseStatusDto sellerRequest(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.sellerRequest(userDetails.getUser().getId());
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(HttpServletRequest request) {
        TokenResponseDto tokenResponseDto = userService.reissueToken(jwtProvider.resolveRefreshToken(request));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JwtProvider.ACCESS_TOKEN_HEADER, tokenResponseDto.getAccessToken());
        responseHeaders.set(JwtProvider.REFRESH_TOKEN_HEADER, tokenResponseDto.getRefreshToken());
        return new ResponseEntity<>(tokenResponseDto, responseHeaders, HttpStatus.OK);
    }

}
