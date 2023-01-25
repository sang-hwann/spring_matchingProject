package com.project.matchingsystem.controller;

import com.project.matchingsystem.dto.request.SignUpAdminRequestDto;
import com.project.matchingsystem.dto.response.ResponseStatusDto;
import com.project.matchingsystem.dto.response.SellerManagementResponseDto;
import com.project.matchingsystem.dto.response.UserResponseDto;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @PostMapping("/sign-up")
    public ResponseStatusDto signUp(@Validated @RequestBody SignUpAdminRequestDto signUpAdminRequestDto) {
        if (!signUpAdminRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_AUTH_TOKEN.getMessage());
        }
        return adminService.signUpAdmin(signUpAdminRequestDto);
    }

    @GetMapping("/users")
    public List<UserResponseDto> getUsers(Pageable pageable) {
        return adminService.getUsers(pageable).getContent();
    }

    @GetMapping("/seller-apply")
    public List<SellerManagementResponseDto> getSellerManagements(Pageable pageable) {
        return adminService.getSellerManagements(pageable).getContent();
    }

    @PutMapping("/seller-managements/{sellerManagementId}/permit")
    public ResponseStatusDto permitSellerRole(@PathVariable Long sellerManagementId) {
        return adminService.permitSellerRole(sellerManagementId);
    }

    @PutMapping("/seller-managements/{sellerManagementId}/drop")
    public ResponseStatusDto dropSellerRole(@PathVariable Long sellerManagementId) {
        return adminService.dropSellerRole(sellerManagementId);
    }

    @PutMapping("/seller-managements/{sellerManagementId}/reject")
    public ResponseStatusDto rejectSellerRole(@PathVariable Long sellerManagementId) {
        return adminService.rejectSellerRole(sellerManagementId);
    }

}
