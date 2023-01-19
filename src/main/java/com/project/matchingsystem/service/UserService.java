package com.project.matchingsystem.service;

import com.project.matchingsystem.domain.SellerManagement;
import com.project.matchingsystem.domain.SellerManagementStatusEnum;
import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.domain.UserProfile;
import com.project.matchingsystem.domain.UserRoleEnum;
import com.project.matchingsystem.dto.*;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.jwt.JwtProvider;
import com.project.matchingsystem.repository.UserProfileRepository;
import com.project.matchingsystem.repository.SellerManagementRepository;
import com.project.matchingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserProfileRepository userProfileRepository;
    private final SellerManagementRepository sellerManagementRepository;

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
        UserProfile userProfile = new UserProfile(user, nickname);
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

    public ResponseStatusDto signOut(String accessToken, String username) {
        // Redis 에서 해당 Username 으로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제
        if (redisTemplate.opsForValue().get("RT:" + username) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + username);
        }

        // 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장
        Long expiration = jwtProvider.getExpiration(accessToken);
        redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return new ResponseStatusDto(HttpStatus.OK.toString(), "로그아웃 성공");
    }

    @Transactional
    public UserProfileResponseDto getUserProfile(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );
        return new UserProfileResponseDto(userProfile);
    }

    @Transactional
    public UserProfileResponseDto updateUserProfile(UserProfileRequestDto userProfileRequestDto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );
        UserProfile userProfile = userProfileRepository.findByUserId(user.getId()).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );
        userProfile.update(userProfileRequestDto);
        return new UserProfileResponseDto(userProfile);
    }

    @Transactional
    public ResponseStatusDto sellerRequest(Long sellerManagementId) {

        //신청자가 관리자일때 생략하기
        if (userRepository.findById(sellerManagementId).get().getUserRole()==UserRoleEnum.ADMIN) {
            return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "해당 관리자는 권한요청이 불가능합니다. ");
        }

        //요청 기록이 있을때
        if (sellerManagementRepository.existsById(sellerManagementId)) {

            SellerManagement sellerManagement = sellerManagementRepository.findByUserId(sellerManagementId).orElseThrow(
                    () -> new IllegalArgumentException(ErrorCode.NOT_FIND_REQUEST.getMessage())
            );

            if (sellerManagement.getRequestStatus()== SellerManagementStatusEnum.WAIT) {
                return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "이미 신청중 상태입니다.");
            }

            if (sellerManagement.getRequestStatus()==SellerManagementStatusEnum.COMPLETE) {
                return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "이미 권한 COMPLETE상태 입니다.");
            }
            //Drop일때는 에러
            if (sellerManagement.getRequestStatus()==SellerManagementStatusEnum.DROP) {
                return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "거절 상태로(DROP) 신청 불가능 ");
            }

            if (sellerManagement.getRequestStatus()==SellerManagementStatusEnum.REJECT) {
                sellerManagement.waitRequestStatus(); //신청한 요청상태만 wait으로 전환
                return new ResponseStatusDto(HttpStatus.OK.toString(), "판매자 권한 승인 재요청완료");
            }

            //상태를 wait으로 전환
            sellerManagement = new SellerManagement(sellerManagementId, SellerManagementStatusEnum.WAIT);
            sellerManagement.waitRequestStatus();
            return new ResponseStatusDto(HttpStatus.OK.toString(), "판매자 권한 승인 요청완료");
        }

        // 이미 신청wait상태일때도 신청안되게하기
        //이미 신청시 , 다시 wait로 전환

        //요청 기록이 없을때
        if(!(sellerManagementRepository.existsById(sellerManagementId))) {
            SellerManagement sellerManagement = new SellerManagement(sellerManagementId, SellerManagementStatusEnum.WAIT);

            sellerManagementRepository.save(sellerManagement);

            return new ResponseStatusDto(HttpStatus.OK.toString(), "판매자 권한 승인 요청완료");
        }
        return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "판매자 권한 승인 요청에러");
    }
}
