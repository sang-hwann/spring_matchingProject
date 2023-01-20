package com.project.matchingsystem.domain;

import com.project.matchingsystem.dto.ItemRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Item extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    private String imagePath;

    @Column(nullable = false)
    private String description;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ItemTransactionStatusEnum itemTransactionStatusEnum;

    public Item(ItemRequestDto itemRequestDto, Category category, User user) {
        this.itemName = itemRequestDto.getItemName();
        this.imagePath = itemRequestDto.getImagePath();
        this.description = itemRequestDto.getDescription();
        this.price = itemRequestDto.getPrice();
        this.itemTransactionStatusEnum = ItemTransactionStatusEnum.FOR_SALE;
        this.user = user;
        this.category = category;
    }

    public void update(ItemRequestDto itemRequestDto, Category category) {
        this.itemName = itemRequestDto.getItemName();
        this.imagePath = itemRequestDto.getImagePath();
        this.description = itemRequestDto.getDescription();
        this.price = itemRequestDto.getPrice();
        this.category = category;
    }

}
