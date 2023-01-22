package com.project.matchingsystem.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
public class ItemRequestDto {

    @NotBlank
    @Size(min = 2 , max = 20)
    private String itemName;

    private String imagePath;

    @NotBlank
    @Size(min = 10 , max = 500)
    private String description;

    @Min(0)
    @Max(100000000)
    private int price;

    @NotNull
    private String categoryName;

}
