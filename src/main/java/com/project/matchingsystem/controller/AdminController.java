package com.sparta.matchingsystem.controller;

import com.sparta.matchingsystem.dto.ResponseStatusDto;
import com.sparta.matchingsystem.dto.SellerRoleApplyFormResponseDto;
import com.sparta.matchingsystem.dto.UserResponseDto;
import com.sparta.matchingsystem.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public List<UserResponseDto> getUsers(Pageable pageable) {
        return adminService.getUsers(pageable).getContent();
    }

    @GetMapping("/seller-requests")
    public List<SellerRoleApplyFormResponseDto> getSellerRoleApplyForms(Pageable pageable) {
        return adminService.getSellerRoleApplyForms(pageable).getContent();
    }

    @PostMapping("/seller-apply-forms/{sellerRoleApplyFormId}")
    public ResponseStatusDto permitSellerRole(@PathVariable Long sellerRoleApplyFormId) {
        return adminService.permitSellerRole(sellerRoleApplyFormId);
    }

    @DeleteMapping("/seller-apply-forms/{sellerRoleApplyFormId}")
    public ResponseStatusDto dropSellerRole(@PathVariable Long sellerRoleApplyFormId) {
        return adminService.dropSellerRole(sellerRoleApplyFormId);
    }

}
