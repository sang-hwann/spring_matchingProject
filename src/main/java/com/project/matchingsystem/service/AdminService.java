package com.sparta.matchingsystem.service;

import com.sparta.matchingsystem.dto.ResponseStatusDto;
import com.sparta.matchingsystem.dto.SellerRoleApplyFormResponseDto;
import com.sparta.matchingsystem.dto.UserResponseDto;
import com.sparta.matchingsystem.repository.UserRepository;
import com.sparta.matchingsystem.repository.SellerRoleApplyFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {

    private UserRepository userRepository;
    private SellerRoleApplyFormRepository sellerRoleApplyFormRepository;

    public Page<UserResponseDto> getUsers(Pageable pageable) {
        return null;
    }

    public Page<SellerRoleApplyFormResponseDto> getSellerRoleApplyForms(Pageable pageable) {
        return null;
    }

    public ResponseStatusDto permitSellerRole(Long sellerRequestId) {
        return null;
    }

    public ResponseStatusDto dropSellerRole(Long sellerRoleApplyFormId) {
        return null;
    }

}
