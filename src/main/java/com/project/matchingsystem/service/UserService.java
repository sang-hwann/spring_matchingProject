package com.project.matchingsystem.service;

import com.project.matchingsystem.domain.*;
import com.project.matchingsystem.dto.*;
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
import org.springframework.util.ObjectUtils;



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

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException(ErrorCode.DUPLICATED_USERNAME.getMessage());
        }
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new IllegalArgumentException(ErrorCode.DUPLICATED_NICKNAME.getMessage());
        }

        UserRoleEnum role = UserRoleEnum.USER;
        User user = new User(username, password, role,nickname);
        userRepository.save(user);
        return new ResponseStatusDto(HttpStatus.OK.toString(), "회원가입 완료");
    }

    @Transactional
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

    @Transactional
    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );
        return new UserProfileResponseDto(user);
    }

    @Transactional
    public TokenResponseDto reissueToken(String refreshToken){
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

        return new TokenResponseDto(jwtProvider.createAccessToken(username,user.getUserRole()),issueRefreshToken(username));
    }

    @Transactional
    public String issueRefreshToken(String username){
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
    public ResponseStatusDto sellerRequest(User user) {

        Long userId = user.getId();

        //신청자가 관리자일때 생략하기
        if (userRepository.findById(userId).get().getUserRole()==UserRoleEnum.ADMIN) {
            return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "해당 관리자는 권한요청이 불가능합니다. ");
        }

        //요청 기록이 있을때
        if (sellerManagementRepository.existsById(userId)) {

            SellerManagement sellerManagement = sellerManagementRepository.findByUserId(userId).orElseThrow(
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
            sellerManagement = new SellerManagement(userId, SellerManagementStatusEnum.WAIT);
            sellerManagement.waitRequestStatus();
            return new ResponseStatusDto(HttpStatus.OK.toString(), "판매자 권한 승인 요청완료");
        }

        // 이미 신청wait상태일때도 신청안되게하기
        //이미 신청시 , 다시 wait로 전환

        //요청 기록이 없을때
        if(!(sellerManagementRepository.existsById(userId))) {
            SellerManagement sellerManagement = new SellerManagement(userId, SellerManagementStatusEnum.WAIT);

            sellerManagementRepository.save(sellerManagement);

            return new ResponseStatusDto(HttpStatus.OK.toString(), "판매자 권한 승인 요청완료");
        }
        return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "판매자 권한 승인 요청에러");
    }
}
