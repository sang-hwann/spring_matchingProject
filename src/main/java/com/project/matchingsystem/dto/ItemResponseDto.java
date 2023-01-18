package com.project.matchingsystem.dto;

import com.project.matchingsystem.domain.Item;
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

    public ItemResponseDto(Item item) {
        this.itemName = item.getItemName();
        this.image = item.getImage();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.createdAt = item.getCreatedAt();
        this.modifiedAt = item.getModifiedAt();
    }

    private List<ItemResponseDto> itemList = new ArrayList<>();


}
