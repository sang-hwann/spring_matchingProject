package com.project.matchingsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ItemRequestDto {

    @NotBlank @Min(2) @Max(20)
    private String itemName;

    private String imagePath;

    @NotBlank @Min(10) @Max(500)
    private String description;

    @Min(0) @Max(100000000)
    private int price;

    @NotNull
    private String categoryName;

}
