package com.project.matchingsystem.controller;

import com.project.matchingsystem.dto.CategoryRequestDto;
import com.project.matchingsystem.dto.CategoryResponseDto;
import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryResponseDto> getCategories(@PageableDefault(size = 5) Pageable pageable){
        return categoryService.getCategories(pageable).getContent();
    }

    @PostMapping("/admin/categories")
    public ResponseStatusDto createParentCategory(@RequestBody CategoryRequestDto categoryRequestDto){
        return categoryService.createParentCategory(categoryRequestDto);
    }

    @PostMapping("/admin/categories/{parentId}")
    public ResponseStatusDto createChildCategory(@PathVariable Long parentId, @RequestBody CategoryRequestDto categoryRequestDto){
        return categoryService.createChildCategory(parentId, categoryRequestDto);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseStatusDto updateCategory(@PathVariable Long categoryId,@RequestBody CategoryRequestDto categoryRequestDto){
        return categoryService.updateCategory(categoryId, categoryRequestDto);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseStatusDto deleteCategory(@PathVariable Long categoryId){
        return categoryService.deleteCategory(categoryId);
    }
}
