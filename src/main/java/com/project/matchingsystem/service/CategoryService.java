package com.project.matchingsystem.service;

import com.project.matchingsystem.domain.Category;
import com.project.matchingsystem.dto.CategoryRequestDto;
import com.project.matchingsystem.dto.CategoryResponseDto;
import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.repository.CategoryRepository;
import com.project.matchingsystem.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public Page<CategoryResponseDto> getCategories(Pageable pageable) {
        List<CategoryResponseDto> list = categoryRepository.findByParentIdIsNull(pageable).stream()
                .map(c -> c.toCategoryResponseDto(categoryRepository.findByParentIdIsNotNull())).collect(Collectors.toList());
        return new PageImpl<>(list);
    }

    @Transactional
    public ResponseStatusDto createParentCategory(CategoryRequestDto categoryRequestDto) {
        categoryRepository.findByCategoryName(categoryRequestDto.getCategoryName()).ifPresent(category -> {
            throw new IllegalArgumentException(ErrorCode.DUPLICATED_CATEGORY.getMessage());
        });

        Category category = new Category(categoryRequestDto.getCategoryName());
        categoryRepository.save(category);
        return new ResponseStatusDto(HttpStatus.OK.toString(), category.getCategoryName() + " 카테고리 생성 완료");
    }

    @Transactional
    public ResponseStatusDto createChildCategory(Long parentId, CategoryRequestDto categoryRequestDto) {
        categoryRepository.findByCategoryName(categoryRequestDto.getCategoryName()).ifPresent(category -> {
            throw new IllegalArgumentException(ErrorCode.DUPLICATED_CATEGORY.getMessage());
        });
        Category findCategory = categoryRepository.findById(parentId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
        );
        if (findCategory.getParentId() != null) {
            throw new IllegalArgumentException(ErrorCode.NOT_PARENT_CATEGORY.getMessage());
        }
        //기본 카테고리에 상품이 등록되어있으면 하위 카테고리 생성 불가
        itemRepository.findByCategory(findCategory).ifPresent(item -> {
            throw new IllegalArgumentException(ErrorCode.NOT_EMPTY_CATEGORY.getMessage());
        });
        Category category = new Category(categoryRequestDto.getCategoryName(), parentId);
        categoryRepository.save(category);
        return new ResponseStatusDto(HttpStatus.OK.toString(), category.getCategoryName() + " 카테고리 생성 완료");
    }

    @Transactional
    public ResponseStatusDto updateCategory(Long categoryId, CategoryRequestDto categoryRequestDto) {
        categoryRepository.findByCategoryName(categoryRequestDto.getCategoryName()).ifPresent(category1 -> {
            throw new IllegalArgumentException(ErrorCode.DUPLICATED_CATEGORY.getMessage());
        });
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
        );
        category.updateCategory(categoryRequestDto.getCategoryName());
        categoryRepository.save(category);
        return new ResponseStatusDto(HttpStatus.OK.toString(), category.getCategoryName() + " 카테고리 수정 완료");
    }

    @Transactional
    public ResponseStatusDto deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
        );
        String categoryName = category.getCategoryName();
        categoryRepository.deleteByParentId(categoryId);
        categoryRepository.delete(category);
        return new ResponseStatusDto(HttpStatus.OK.toString(), categoryName + " 카테고리 삭제 완료");
    }

}
