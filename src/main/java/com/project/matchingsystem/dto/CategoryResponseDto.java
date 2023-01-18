package com.project.matchingsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CategoryResponseDto {
    private String categoryName;

    public CategoryResponseDto(String categoryName) {
        this.categoryName = categoryName;
    }
}
