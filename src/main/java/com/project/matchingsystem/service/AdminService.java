package com.project.matchingsystem.service;

import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.dto.SellerRoleApplyFormResponseDto;
import com.project.matchingsystem.dto.UserResponseDto;
import com.project.matchingsystem.repository.UserRepository;
import com.project.matchingsystem.repository.SellerRoleApplyFormRepository;
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
