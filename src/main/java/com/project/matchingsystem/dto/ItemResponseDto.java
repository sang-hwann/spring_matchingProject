package com.project.matchingsystem.dto;

import com.project.matchingsystem.domain.Item;
import com.project.matchingsystem.domain.ItemTransactionStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ItemResponseDto {

    private String itemName;
    private String imagePath;
    private String description;
    private int price;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private ItemTransactionStatusEnum itemTransactionStatusEnum;
    private String nickname;
    private Long itemId;
    private String categoryName;

    public ItemResponseDto(Item item, String nickname) {
        this.itemName = item.getItemName();
        this.imagePath = item.getImagePath();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.createdAt = item.getCreatedAt();
        this.modifiedAt = item.getModifiedAt();
        this.itemTransactionStatusEnum = item.getItemTransactionStatusEnum();
        this.nickname = nickname;
        this.itemId = item.getId();
        this.categoryName = item.getCategory().getCategoryName();
    }

}
