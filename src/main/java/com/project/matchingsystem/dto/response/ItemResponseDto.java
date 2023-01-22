package com.project.matchingsystem.dto.response;

import com.project.matchingsystem.domain.Item;
import com.project.matchingsystem.enums.ItemTransactionStatusEnum;
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
    private ItemTransactionStatusEnum transactionStatus;
    private String nickname;
    private Long itemId;
    private String categoryName;

    public ItemResponseDto(Item item, String nickname) {
        this.itemName = item.getName();
        this.imagePath = item.getImagePath();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.createdAt = item.getCreatedAt();
        this.modifiedAt = item.getModifiedAt();
        this.transactionStatus = item.getItemTransactionStatusEnum();
        this.nickname = nickname;
        this.itemId = item.getId();
        this.categoryName = item.getCategory().getName();
    }

}
