package com.project.matchingsystem.dto;

import com.project.matchingsystem.domain.Transaction;
import lombok.Getter;

@Getter
public class TransactionResponseDto {

    private final Long id;
    private final String nickname;
    private final String itemName;
    private final String status;
    private final String createdAt;
    private final String modifiedAt;

    public TransactionResponseDto(Transaction transaction, String nickname) {
        this.id = transaction.getId();
        this.nickname = nickname;
        this.itemName = transaction.getItem().getItemName();
        this.status = transaction.getTransactionStatus().name();
        this.createdAt = transaction.getCreatedAt().toString();
        this.modifiedAt = transaction.getModifiedAt().toString();
    }

}
