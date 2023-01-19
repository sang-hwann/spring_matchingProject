package com.project.matchingsystem.controller;

import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.dto.SellerManagementResponseDto;
import com.project.matchingsystem.dto.UserResponseDto;
import com.project.matchingsystem.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/seller-apply")
    public List<SellerManagementResponseDto> getSellerRoleApplyForms(Pageable pageable) {
        return adminService.getSellerRoleApplyForms(pageable).getContent();
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
