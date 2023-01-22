package com.project.matchingsystem.domain;

import com.project.matchingsystem.enums.SellerManagementStatusEnum;
import com.project.matchingsystem.dto.response.SellerManagementResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SellerManagement extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_management_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SellerManagementStatusEnum requestStatus;

    public SellerManagement(Long userId, SellerManagementStatusEnum requestStatus) {
        this.userId = userId;
        this.requestStatus = requestStatus;
    }

    public void completeRequestStatus() {
        this.requestStatus = SellerManagementStatusEnum.COMPLETE;
    }

    public void waitRequestStatus() {
        this.requestStatus = SellerManagementStatusEnum.WAIT;
    }

    public void dropRequestStatus() {
        this.requestStatus = SellerManagementStatusEnum.DROP;
    }

    public void rejectRequestStatus() {
        this.requestStatus = SellerManagementStatusEnum.REJECT;
    }

    public SellerManagementResponseDto toSellerManagementResponseDto() {
        return new SellerManagementResponseDto(this);
    }

}
