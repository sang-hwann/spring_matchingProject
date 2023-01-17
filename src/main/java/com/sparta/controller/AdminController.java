package com.sparta.controller;

import com.sparta.dto.ResponseStatusDto;
import com.sparta.dto.SellerRoleApplyFormResponseDto;
import com.sparta.dto.UserResponseDto;
import com.sparta.service.AdminService;
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
