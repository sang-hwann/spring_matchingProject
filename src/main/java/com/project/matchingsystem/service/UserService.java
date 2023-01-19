package com.project.matchingsystem.service;

import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.domain.UserRoleEnum;
import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.dto.SignInRequestDto;
import com.project.matchingsystem.dto.SignUpRequestDto;
import com.project.matchingsystem.dto.TokenResponseDto;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.jwt.JwtProvider;
import com.project.matchingsystem.repository.UserRepository;
import com.project.matchingsystem.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResponseStatusDto signUp(SignUpRequestDto signUpRequestDto) {
        String username = signUpRequestDto.getUsername();
        String password = passwordEncoder.encode(signUpRequestDto.getPassword());

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException(ErrorCode.DUPLICATED_USERNAME.getMessage());
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (signUpRequestDto.isAdmin()) {
            if (!signUpRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException(ErrorCode.INVALID_AUTH_TOKEN.getMessage());
            }
            role = UserRoleEnum.ADMIN;
        }
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
        String refreshToken = issueRefreshToken(username);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    public ResponseStatusDto signOut() {
        return null;
    }

    public ResponseStatusDto applySellerRole() {
        return null;
    }

    @Transactional
    public TokenResponseDto reissueToken(String refreshToken){
        String username = jwtProvider.getUserInfoFromToken(refreshToken).getSubject();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );

        if (redisUtil.isExistsRefreshToken(username)) {
            if (redisUtil.getRefreshToken(username).equals(refreshToken)) {
            } else {
                throw new IllegalArgumentException(ErrorCode.INVALID_TOKEN.getMessage());
            }
        } else {
            throw new IllegalArgumentException(ErrorCode.INVALID_TOKEN.getMessage());
        }

        return new TokenResponseDto(jwtProvider.createAccessToken(username,user.getUserRole()),issueRefreshToken(username));
    }

    @Transactional
    public String issueRefreshToken(String username){
        String refreshToken = jwtProvider.createRefreshToken(username);
        String tokenValue = refreshToken.substring(7);

        redisUtil.setRefreshToken(username, tokenValue);

        return refreshToken;
    }

}
