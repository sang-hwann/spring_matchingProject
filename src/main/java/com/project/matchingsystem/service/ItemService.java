package com.project.matchingsystem.service;

import com.project.matchingsystem.domain.Category;
import com.project.matchingsystem.domain.Item;
import com.project.matchingsystem.domain.Transaction;
import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.dto.ItemRequestDto;
import com.project.matchingsystem.dto.ItemResponseDto;
import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.repository.CategoryRepository;
import com.project.matchingsystem.repository.ItemRepository;
import com.project.matchingsystem.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    // 상품 이름 검색
    @Transactional
    public Page<ItemResponseDto> searchItems(String itemName, Pageable pageable) {
        List<Item> itemList = itemRepository.findAllByItemNameContainingOrderByModifiedAtDesc(itemName, pageable);
        List<ItemResponseDto> itemResponseDto = new ArrayList<>();
        itemList.forEach(item -> itemResponseDto.add(new ItemResponseDto(item, item.getUser().getNickname())));
        return new PageImpl<>(itemResponseDto);
    }

    // 특정 상품 조회
    @Transactional(readOnly = true)
    public ItemResponseDto getItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_ITEM.getMessage()));
        return new ItemResponseDto(item, item.getUser().getUsername());
    }

    // 전체 상품 조회
    @Transactional(readOnly = true)
    public Page<ItemResponseDto> getItems(Pageable pageable) {
        List<Item> itemList = itemRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<ItemResponseDto> itemResponseDto = new ArrayList<>();
        itemList.forEach(item -> itemResponseDto.add(new ItemResponseDto(item, item.getUser().getNickname())));
        return new PageImpl<>(itemResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<ItemResponseDto> getItemsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
        );
        List<Item> itemList = itemRepository.findByCategoryOrderByCreatedAtDesc(category, pageable);
        List<ItemResponseDto> itemResponseDto = new ArrayList<>();
        itemList.forEach(item -> itemResponseDto.add(new ItemResponseDto(item, item.getUser().getNickname())));
        return new PageImpl<>(itemResponseDto);
    }

    // 판매자 상품 조회
    @Transactional(readOnly = true)
    public Page<ItemResponseDto> getItemsBySeller(Long sellerId, Pageable pageable) {
        List<Item> itemList = itemRepository.findAllByUserIdOrderByCreatedAtDesc(sellerId, pageable);
        List<ItemResponseDto> itemResponseDto = new ArrayList<>();
        itemList.forEach(item -> itemResponseDto.add(new ItemResponseDto(item, item.getUser().getNickname())));
        return new PageImpl<>(itemResponseDto);
    }

    // 상품 등록
    @Transactional
    public ResponseStatusDto uploadItem(ItemRequestDto itemRequestDto, User user) {
        // 카테고리가 존재 하는지 확인
        Category category = categoryRepository.findByCategoryName(itemRequestDto.getCategoryName()).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
        );
        if(category.getParentId() == null){
            throw new IllegalArgumentException(ErrorCode.NOT_CHILD_CATEGORY.getMessage());
        }
        itemRepository.save(new Item(itemRequestDto, category, user));
        return new ResponseStatusDto(HttpStatus.OK.toString(), "상품 등록 완료");
    }

    @Transactional
    public ResponseStatusDto updateItem(Long itemId, ItemRequestDto itemRequestDto, String username) {
        // 카테고리가 존재 하는지 확인
        Category category = categoryRepository.findByCategoryName(itemRequestDto.getCategoryName()).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
        );
        if(category.getParentId() == null){
            throw new IllegalArgumentException(ErrorCode.NOT_CHILD_CATEGORY.getMessage());
        }
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_ITEM.getMessage()));
        if (item.getUser().getUsername().equals(username)) {
            item.update(itemRequestDto, category);
        } else {
            throw new IllegalArgumentException(ErrorCode.AUTHORIZATION.getMessage());
        }

        return new ResponseStatusDto(HttpStatus.OK.toString(), "상품 수정 완료");
    }

    //상품 삭제(판매자)
    @Transactional
    public ResponseStatusDto deleteItem(Long itemId, String username) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_ITEM.getMessage()));
        if (item.getUser().getUsername().equals(username)) {
            itemRepository.delete(item);
        } else {
            throw new IllegalArgumentException(ErrorCode.AUTHORIZATION.getMessage());
        }
        transactionRepository.findByItemId(itemId).forEach(Transaction::updateStatusToDelete);

        return new ResponseStatusDto(HttpStatus.OK.toString(), "상품 삭제 완료");
    }

    //상품 삭제(관리자)
    @Transactional
    public ResponseStatusDto deleteItemByAdmin(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_ITEM.getMessage()));
        itemRepository.delete(item);
        return new ResponseStatusDto(HttpStatus.OK.toString(), "상품 삭제 완료");
    }

}
