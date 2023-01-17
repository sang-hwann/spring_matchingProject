package com.sparta.service;

import com.sparta.dto.ResponseStatusDto;
import com.sparta.dto.SellerRoleApplyFormResponseDto;
import com.sparta.dto.UserResponseDto;
import com.sparta.repository.SellerRoleApplyFormRepository;
import com.sparta.repository.UserRepository;
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
