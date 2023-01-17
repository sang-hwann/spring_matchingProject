package com.project.matchingsystem.service;

import com.project.matchingsystem.dto.CategoryRequestDto;
import com.project.matchingsystem.dto.CategoryResponseDto;
import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Page<CategoryResponseDto> getCategories(Pageable pageable) {
        return null;
    }

    public ResponseStatusDto createCategory(CategoryRequestDto categoryRequestDto) {
        return null;
    }

    public ResponseStatusDto deleteCategory(Long categoryId) {
        return null;
    }

    public ResponseStatusDto updateCategory(CategoryRequestDto categoryRequestDto) {
        return null;
    }

}
