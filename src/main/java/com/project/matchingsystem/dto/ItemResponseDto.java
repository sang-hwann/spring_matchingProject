package com.project.matchingsystem.dto;

import com.project.matchingsystem.domain.Item;
import com.project.matchingsystem.domain.ItemTransactionStatusEnum;
import com.project.matchingsystem.repository.UserProfileRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ItemResponseDto {

    private String itemName;
    private byte[] image;
    private String description;
    private int price;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private ItemTransactionStatusEnum itemTransactionStatusEnum;
    private String nickname;

    public ItemResponseDto(Item item, String nickname) {
        this.itemName = item.getItemName();
        this.image = item.getImage();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.createdAt = item.getCreatedAt();
        this.modifiedAt = item.getModifiedAt();
        this.itemTransactionStatusEnum = item.getItemTransactionStatusEnum();
        this.nickname = nickname;
    }

}
