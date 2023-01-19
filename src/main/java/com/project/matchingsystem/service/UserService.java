package com.project.matchingsystem.service;

import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.domain.UserProfile;
import com.project.matchingsystem.domain.UserRoleEnum;
import com.project.matchingsystem.dto.*;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.jwt.JwtProvider;
import com.project.matchingsystem.repository.UserProfileRepository;
import com.project.matchingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public ResponseStatusDto signUp(SignUpRequestDto signUpRequestDto) {
        String username = signUpRequestDto.getUsername();
        String password = passwordEncoder.encode(signUpRequestDto.getPassword());
        String nickname = signUpRequestDto.getNickname();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException(ErrorCode.DUPLICATED_USERNAME.getMessage());
        }
        if (userProfileRepository.findByNickname(nickname).isPresent()) {
            throw new IllegalArgumentException(ErrorCode.DUPLICATED_NICKNAME.getMessage());
        }

        UserRoleEnum role = UserRoleEnum.USER;
        User user = new User(username, password, role);
        userRepository.save(user);
        userProfileRepository.save(userProfile);
        return new ResponseStatusDto(HttpStatus.OK.toString(), "회원가입 완료");
    }

    public ResponseStatusDto signUpAdmin(SignUpAdminRequestDto signUpAdminRequestDto) {
        String username = signUpAdminRequestDto.getUsername();
        String password = passwordEncoder.encode(signUpAdminRequestDto.getPassword());

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException(ErrorCode.DUPLICATED_USERNAME.getMessage());
        }

        UserRoleEnum role = UserRoleEnum.ADMIN;
        User user = new User(username, password, role);
        userRepository.save(user);
        return new ResponseStatusDto(HttpStatus.OK.toString(), "회원가입 완료");
    }

    @Transactional
    public TokenResponseDto signIn(SignInRequestDto signInRequestDto) {
        String username = signInRequestDto.getUsername();
        String password = signInRequestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException(ErrorCode.INVALID_PASSWORD.getMessage());
        }
        String accessToken = jwtProvider.createAccessToken(username, user.getUserRole());
        String refreshToken = jwtProvider.createRefreshToken(username);


        return new TokenResponseDto(accessToken, refreshToken);
    }

    public ResponseStatusDto signOut() {
        return null;
    }

    public ResponseStatusDto applySellerRole() {
        return null;
    }

    @Transactional
    public UserProfileResponseDto getUserProfile(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        return new UserProfileResponseDto(userProfile);
    }

    @Transactional
    public UserProfileResponseDto updateUserProfile(UserProfileRequestDto userProfileRequestDto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );
        UserProfile userProfile = userProfileRepository.findByUserId(user.getId());
        userProfile.update(userProfileRequestDto);
        return new UserProfileResponseDto(userProfile);
    }
}
