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

        SellerManagement sellerManagement = sellerManagementRepository.findById(sellerManagementId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FIND_REQUEST.getMessage())
        );

        //유저 데이터찾기
        User user = userRepository.findById(sellerManagement.getUserId()).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );

        // 요청상태가 drop확인 - DROP되면 다시는 처리 더이상 안됨
        if (sellerManagement.getRequestStatus() == SellerManagementStatusEnum.DROP) {
            throw new IllegalArgumentException(ErrorCode.ALREADY_SELLER_MANAGEMENT_STATUS_DROP.getMessage());
        }
        // 요청상태가 COMPLETE확인 - COMPLETE되면 다시는 처리 더이상 안됨
        if (sellerManagement.getRequestStatus() == SellerManagementStatusEnum.COMPLETE) {
            throw new IllegalArgumentException(ErrorCode.ALREADY_SELLER_MANAGEMENT_STATUS_COMPLETE.getMessage());
        }
        // 요청상태가 REJECT확인 - COMPLETE되면 다시는 처리 더이상 안됨
        if (sellerManagement.getRequestStatus() == SellerManagementStatusEnum.REJECT) {
            throw new IllegalArgumentException(ErrorCode.ALREADY_SELLER_MANAGEMENT_STATUS_REJECT.getMessage());
        }

        user.permitRoleUser(); //유저 권한 판매자로 변경
        sellerManagement.completeRequestStatus(); //권한 요청 데이터의 요청상태 complete로 전환

        return new ResponseStatusDto(HttpStatus.OK.toString(), "판매자 권한 승인 완료했습니다.");
    }

    @Transactional
    public ResponseStatusDto dropSellerRole(Long sellerManagementId) {
        SellerManagement sellerManagement = sellerManagementRepository.findById(sellerManagementId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FIND_REQUEST.getMessage())
        );
        //유저 데이터찾기
        User user = userRepository.findById(sellerManagement.getUserId()).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );
        // 요청상태가 drop확인 - DROP되면 다시는 처리 더이상 안됨
        if (sellerManagement.getRequestStatus() == SellerManagementStatusEnum.DROP) {
            throw new IllegalArgumentException(ErrorCode.ALREADY_SELLER_MANAGEMENT_STATUS_DROP.getMessage());
        }
        //요청상태 Complete 시 더이상 처리 안함
        if (sellerManagement.getRequestStatus() == SellerManagementStatusEnum.COMPLETE) {
            user.dropRoleUser(); //유저 권한 user로 변경
            sellerManagement.dropRequestStatus(); //권한 요청 데이터의 요청상태 drop 전환
            return new ResponseStatusDto(HttpStatus.OK.toString(), "판매자 권한 회수 처리 완료했습니다.");
        }
        return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), "판매자 권한 회수 처리 할 수 없습니다.");
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

        //요청 상태 Complete 시 더이상 처리 안함
        if (sellerManagement.getRequestStatus() == SellerManagementStatusEnum.COMPLETE) {
            throw new IllegalArgumentException(ErrorCode.ALREADY_SELLER_MANAGEMENT_STATUS_COMPLETE.getMessage());
        }

        // 요청 상태가 WAIT확인 - DROP되면 다시는 안되는거 알려주기 wait을 cancel로
        if (sellerManagement.getRequestStatus() != SellerManagementStatusEnum.WAIT) {
            throw new IllegalArgumentException(ErrorCode.NOT_SELLER_MANAGEMENT_STATUS_WAIT.getMessage());
        }

        user.dropRoleUser(); //유저 권한 user로 변경
        sellerManagement.rejectRequestStatus(); //권한 요청 데이터의 요청상태 reject로 전환
        return new ResponseStatusDto(HttpStatus.OK.toString(), "판매자 권한 승인 취소 처리 완료했습니다.");
    }

}
