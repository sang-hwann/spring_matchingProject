package com.project.matchingsystem.service;

import com.project.matchingsystem.domain.SellerManagement;
import com.project.matchingsystem.enums.SellerManagementStatusEnum;
import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.enums.UserRoleEnum;
import com.project.matchingsystem.dto.request.SignInRequestDto;
import com.project.matchingsystem.dto.request.SignUpAdminRequestDto;
import com.project.matchingsystem.dto.request.SignUpRequestDto;
import com.project.matchingsystem.dto.request.UserProfileRequestDto;
import com.project.matchingsystem.dto.response.ResponseStatusDto;
import com.project.matchingsystem.dto.response.TokenResponseDto;
import com.project.matchingsystem.dto.response.UserProfileResponseDto;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.jwt.JwtProvider;
import com.project.matchingsystem.repository.SellerManagementRepository;
import com.project.matchingsystem.repository.UserRepository;
import com.project.matchingsystem.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    private final SellerManagementRepository sellerManagementRepository;

    @Transactional
    public ResponseStatusDto signUp(SignUpRequestDto signUpRequestDto) {
        String username = signUpRequestDto.getUsername();
        String password = passwordEncoder.encode(signUpRequestDto.getPassword());
        String nickname = signUpRequestDto.getNickname();

        userRepository.findByUsername(username).ifPresent(user -> {
            throw new IllegalArgumentException(ErrorCode.DUPLICATED_USERNAME.getMessage());
        });
        userRepository.findByNickname(nickname).ifPresent(user -> {
            throw new IllegalArgumentException(ErrorCode.DUPLICATED_NICKNAME.getMessage());
        });

        UserRoleEnum role = UserRoleEnum.USER;
        User user = new User(username, password, role, nickname);
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

    public ResponseStatusDto signOut(String accessToken, String username) {
        // Redis 에서 해당 Username 으로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제
        if (redisUtil.getRefreshToken(username) != null) {
            // Refresh Token 삭제
            redisUtil.deleteRefreshToken(username);
        }
        // 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장
        Long expiration = jwtProvider.getExpiration(accessToken);
        redisUtil.setAccessTokenInBlackList(accessToken, expiration);

        return new ResponseStatusDto(HttpStatus.OK.toString(), "로그아웃 성공");
    }

    @Transactional(readOnly = true)
    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );
        return new UserProfileResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponseDto> getSellers() {
        return userRepository.findByUserRole(UserRoleEnum.SELLER).stream().map(UserProfileResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public TokenResponseDto reissueToken(String refreshToken) {
        String username = jwtProvider.getUserInfoFromToken(refreshToken).getSubject();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );

        if (redisUtil.isExistsRefreshToken(username)) {
            if (!redisUtil.getRefreshToken(username).equals(refreshToken)) {
                throw new IllegalArgumentException(ErrorCode.INVALID_TOKEN.getMessage());
            }
        } else {
            throw new IllegalArgumentException(ErrorCode.INVALID_TOKEN.getMessage());
        }

        return new TokenResponseDto(jwtProvider.createAccessToken(username, user.getUserRole()), issueRefreshToken(username));
    }

    @Transactional
    public String issueRefreshToken(String username) {
        String refreshToken = jwtProvider.createRefreshToken(username);
        String tokenValue = refreshToken.substring(7);

        redisUtil.setRefreshToken(username, tokenValue);

        return refreshToken;
    }

    public UserProfileResponseDto updateUserProfile(UserProfileRequestDto userProfileRequestDto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );

        user.updateUserProfile(userProfileRequestDto);
        return new UserProfileResponseDto(user);
    }

    @Transactional
    public ResponseStatusDto sellerRequest(Long userId) {
        //신청자가 관리자일때 생략하기
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()));
        if (user.getUserRole() == UserRoleEnum.ADMIN) {
            return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "해당 관리자는 권한 요청이 불가능합니다.");
        }

        //요청 기록이 있을때
        if (sellerManagementRepository.existsByUserId(userId)) {
            SellerManagement sellerManagement = sellerManagementRepository.findByUserId(userId).orElseThrow(
                    () -> new IllegalArgumentException(ErrorCode.NOT_FIND_REQUEST.getMessage())
            );

            if (sellerManagement.getRequestStatus() != SellerManagementStatusEnum.REJECT) {
                throw new IllegalArgumentException(ErrorCode.ALREADY_REQUEST_SELLER.getMessage() + "현재 상태: " + sellerManagement.getRequestStatus());
            }

            sellerManagement = new SellerManagement(userId, SellerManagementStatusEnum.WAIT);
            sellerManagement.waitRequestStatus();
        } else {
            SellerManagement sellerManagement = new SellerManagement(userId, SellerManagementStatusEnum.WAIT);
            sellerManagementRepository.save(sellerManagement);
        }
        return new ResponseStatusDto(HttpStatus.OK.toString(), "판매자 권한 요청 완료");
    }

}
