package com.project.matchingsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequestDto {

    private String itemName;
    private byte[] image;
    private String description;
    private int price;
    private Long categoryId;

}
