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

    @Column(nullable = false)
    private String itemName;

    private byte[] image;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Long categoryId;

    private ItemTransactionStatusEnum itemTransactionStatusEnum;

    public Item(ItemRequestDto itemRequestDto, User user) {
        this.itemName = itemRequestDto.getItemName();
        this.image = itemRequestDto.getImage();
        this.description = itemRequestDto.getDescription();
        this.price = itemRequestDto.getPrice();
        this.itemTransactionStatusEnum = ItemTransactionStatusEnum.FOR_SALE;
        this.user = user;
        this.categoryId = itemRequestDto.getCategoryId();
    }

    public void update(ItemRequestDto itemRequestDto){
        this.itemName = itemRequestDto.getItemName();
        this.image = itemRequestDto.getImage();
        this.description = itemRequestDto.getDescription();
        this.price = itemRequestDto.getPrice();
        this.categoryId = itemRequestDto.getCategoryId();
    }
}
