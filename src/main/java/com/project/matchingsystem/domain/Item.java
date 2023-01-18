package com.project.matchingsystem.domain;

import com.project.matchingsystem.dto.ItemRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Item extends TimeStamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    private byte[] image;

    private String description;

    private int price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private ItemTransactionStatusEnum itemTransactionStatusEnum;

    public Item(ItemRequestDto itemRequestDto, User user) {
        this.itemName = itemRequestDto.getItemName();
        this.image = itemRequestDto.getImage();
        this.description = itemRequestDto.getDescription();
        this.price = itemRequestDto.getPrice();
        this.itemTransactionStatusEnum = ItemTransactionStatusEnum.FOR_SALE;
        this.user= user;
    }

    public void update(ItemRequestDto itemRequestDto){
        this.itemName = itemRequestDto.getItemName();
        this.image = itemRequestDto.getImage();
        this.description = itemRequestDto.getDescription();
        this.price = itemRequestDto.getPrice();
    }
}
