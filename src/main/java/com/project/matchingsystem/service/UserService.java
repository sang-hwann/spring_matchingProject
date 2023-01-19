package com.project.matchingsystem.service;

import com.project.matchingsystem.domain.SellerManagement;
import com.project.matchingsystem.domain.SellerManagementStatusEnum;
import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.domain.UserRoleEnum;
import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.dto.SignInRequestDto;
import com.project.matchingsystem.dto.SignUpRequestDto;
import com.project.matchingsystem.dto.TokenResponseDto;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.jwt.JwtProvider;
import com.project.matchingsystem.repository.SellerManagementRepository;
import com.project.matchingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final SellerManagementRepository sellerManagementRepository;

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
