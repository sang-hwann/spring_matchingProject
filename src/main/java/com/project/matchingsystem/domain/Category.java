package com.project.matchingsystem.domain;

import com.project.matchingsystem.dto.response.CategoryResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Category extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private Long parentId;

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, Long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public CategoryResponseDto toCategoryResponseDto(List<Category> categories) {
        return new CategoryResponseDto(this, categories);
    }

    public void updateCategory(String categoryName) {
        this.name = categoryName;
    }

}
