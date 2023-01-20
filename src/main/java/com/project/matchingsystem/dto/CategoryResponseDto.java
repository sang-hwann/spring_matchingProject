package com.project.matchingsystem.dto;

import com.project.matchingsystem.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class CategoryResponseDto {

    private Long id;
    private String categoryName;
    private final List<CategoryResponseDto> categories = new ArrayList<>();

    public CategoryResponseDto(Category category, List<Category> childCategories) {
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        for (Category childCategory : childCategories) {
            if (childCategory.getParentId().equals(category.getId()) && childCategory.getDepth() == category.getDepth() + 1) {
                categories.add(new CategoryResponseDto(childCategory, childCategories));
            }
        }
    }

}
