package com.project.matchingsystem.domain;

import com.project.matchingsystem.dto.CategoryResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Category extends TimeStamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String categoryName;

    @Column(nullable = false)
    private Long parentId;

    private int depth = 0;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(String categoryName, Long parentId, int depth) {
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.depth = depth;
    }

    public CategoryResponseDto toCategoryResponseDto(List<Category> categories) {
        return new CategoryResponseDto(this, categories);
    }

    public void updateCategory(String categoryName){
        this.categoryName = categoryName;
    }

}
