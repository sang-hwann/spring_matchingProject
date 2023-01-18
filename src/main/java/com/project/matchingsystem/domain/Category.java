package com.project.matchingsystem.domain;

import com.project.matchingsystem.dto.CategoryResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryResponseDto toCategoryResponseDto(){
        return new CategoryResponseDto(categoryName);
    }

    public void updateCategory(String categoryName){
        this.categoryName = categoryName;
    }

}
