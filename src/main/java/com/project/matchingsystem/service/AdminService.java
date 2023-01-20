package com.project.matchingsystem.service;

import com.project.matchingsystem.domain.SellerManagement;
import com.project.matchingsystem.domain.SellerManagementStatusEnum;
import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.dto.SellerManagementResponseDto;
import com.project.matchingsystem.dto.UserResponseDto;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.repository.SellerManagementRepository;
import com.project.matchingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final UserRepository userRepository;
    private final SellerManagementRepository sellerManagementRepository;

    //유저 조회 (유저,판매자,관리자 전부 조회 / 권한도 조회)
    @Transactional
    public Page<UserResponseDto> getUsers(Pageable pageable) {
        List<UserResponseDto> list = userRepository.findAll(pageable).stream().map(User::toUserResponseDto).collect(Collectors.toList());
        return new PageImpl<>(list);
    }

    @Transactional
    public Page<SellerManagementResponseDto> getSellerManagements(Pageable pageable) {
        List<SellerManagementResponseDto> list = sellerManagementRepository.findAll(pageable).stream().map(SellerManagement::toSellerManagementResponseDto).collect(Collectors.toList());
        return new PageImpl<>(list);
    }

    @Transactional
    public ResponseStatusDto permitSellerRole(Long sellerManagementId) {

        SellerManagement sellerManagement = sellerManagementRepository.findByUserId(sellerManagementId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FIND_REQUEST.getMessage())
        );

        //유저 데이터찾기
        User user = userRepository.findById(sellerManagement.getUserId()).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );

        // 요청상태가 drop확인 - DROP되면 다시는 처리 더이상 안됨
        if (sellerManagement.getRequestStatus() == SellerManagementStatusEnum.DROP) {
            return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "거부");
        }
        // 요청상태가 COMPLETE확인 - COMPLETE되면 다시는 처리 더이상 안됨
        if (sellerManagement.getRequestStatus() == SellerManagementStatusEnum.COMPLETE) {
            return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "이미 판매자 승인된 요청입니다.");
        }
        // 요청상태가 REJECT확인 - COMPLETE되면 다시는 처리 더이상 안됨
        if (sellerManagement.getRequestStatus() == SellerManagementStatusEnum.REJECT) {
            return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "취소 처리된 요청입니다.");
        }

        user.permitRoleUser(); //유저 권한 판매자로 변경
        sellerManagement.completeRequestStatus(); //권한 요청 데이터의 요청상태 complete로 전환

        return new ResponseStatusDto(HttpStatus.OK.toString(), "판매자 권한 승인 완료");
    }

    @Transactional
    public ResponseStatusDto dropSellerRole(Long sellerManagementId) {
        SellerManagement sellerManagement = sellerManagementRepository.findByUserId(sellerManagementId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FIND_REQUEST.getMessage())
        );
        //유저 데이터찾기
        User user = userRepository.findById(sellerManagement.getUserId()).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );
        // 요청상태가 drop확인 - DROP되면 다시는 처리 더이상 안됨
        if (sellerManagement.getRequestStatus() == SellerManagementStatusEnum.DROP) {
            return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "거부");
        }
        //요청상태 Complete 시 더이상 처리 안함
        if (sellerManagement.getRequestStatus() == SellerManagementStatusEnum.COMPLETE) {
            user.dropRoleUser(); //유저 권한 user로 변경
            sellerManagement.dropRequestStatus(); //권한 요청 데이터의 요청상태 drop 전환
            return new ResponseStatusDto(HttpStatus.OK.toString(), "판매자 권한 거절처리 완료");
        }
        return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "거절처리 할 수 없습니다.");
    }

    @Transactional
    public ResponseStatusDto rejectSellerRole(Long sellerManagementId) {
        SellerManagement sellerManagement = sellerManagementRepository.findByUserId(sellerManagementId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FIND_REQUEST.getMessage())
        );

        //유저 데이터찾기
        User user = userRepository.findById(sellerManagement.getUserId()).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );

        // 요청 상태가 WAIT확인 - DROP되면 다시는 안되는거 알려주기 wait을 cancel로
        if (sellerManagement.getRequestStatus() != SellerManagementStatusEnum.WAIT) {
            return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "이미 대기상태의 판매자가 아닙니다. 대기 상태에만 적용 가능합니다.");
        }

        //요청 상태 Complete 시 더이상 처리 안함
        if (sellerManagement.getRequestStatus() == SellerManagementStatusEnum.COMPLETE) {
            return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "이미 판매자 승인 된 요청입니다.");
        }

        user.dropRoleUser(); //유저 권한 user로 변경
        sellerManagement.rejectRequestStatus(); //권한 요청 데이터의 요청상태 reject로 전환
        return new ResponseStatusDto(HttpStatus.OK.toString(), "판매자 권한 승인 요청 취소처리 완료");
    }

}
