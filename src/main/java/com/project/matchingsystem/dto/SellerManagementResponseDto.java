package com.project.matchingsystem.dto;

import com.project.matchingsystem.domain.SellerManagementStatusEnum;
import com.project.matchingsystem.domain.SellerManagement;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SellerManagementResponseDto {
    private Long id;
    private Long userId;
    private SellerManagementStatusEnum sellerManagementStatusEnum;
    private String createdAt;
    private String modifiedAt;

    public SellerManagementResponseDto(SellerManagement sellerManagement) {
        this.id = sellerManagement.getId();
        this.userId = sellerManagement.getUserId();
        this.sellerManagementStatusEnum = sellerManagement.getRequestStatus();
        this.createdAt = sellerManagement.getCreatedAt().toString();
        this.modifiedAt = sellerManagement.getModifiedAt().toString();
    }
}
