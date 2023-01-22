package com.project.matchingsystem.dto.response;

import com.project.matchingsystem.domain.SellerManagement;
import com.project.matchingsystem.enums.SellerManagementStatusEnum;
import lombok.Getter;

@Getter
public class SellerManagementResponseDto {

    private final Long id;
    private final Long userId;
    private final SellerManagementStatusEnum sellerManagementStatus;
    private final String createdAt;
    private final String modifiedAt;

    public SellerManagementResponseDto(SellerManagement sellerManagement) {
        this.id = sellerManagement.getId();
        this.userId = sellerManagement.getUserId();
        this.sellerManagementStatus = sellerManagement.getRequestStatus();
        this.createdAt = sellerManagement.getCreatedAt().toString();
        this.modifiedAt = sellerManagement.getModifiedAt().toString();
    }

}
