package com.project.matchingsystem.controller;

import com.project.matchingsystem.dto.ItemRequestDto;
import com.project.matchingsystem.dto.ItemResponseDto;
import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.service.ItemService;
import com.project.matchingsystem.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ItemController {

    private final ItemService itemService;

    // 특정 상품 조회
    @GetMapping("/items/{itemId}")
    public ItemResponseDto getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    // 전체 판매 상품 조회
    @GetMapping("/items")
    public List<ItemResponseDto> getItems(Pageable pageable){
        return itemService.getItems(pageable).getContent();
    }

    // 판매자 상품 조회
    @GetMapping("/seller/{sellerId}/items")
    public List<ItemResponseDto> getItemsBySeller(@PathVariable Long sellerId, Pageable pageable) {
        return itemService.getItemsBySeller(sellerId, pageable).getContent();
    }

    // 판매자 상품 등록
    @PostMapping("/seller/items")
    public ResponseStatusDto uploadItem(@Validated @RequestBody ItemRequestDto itemRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return itemService.uploadItem(itemRequestDto, userDetails.getUser());
    }

    // 판매 상품 수정
    @PutMapping("/seller/items/{itemId}")
    public ResponseStatusDto updateItem(@PathVariable Long itemId, @Validated @RequestBody ItemRequestDto itemRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return itemService.updateItem(itemId, itemRequestDto, userDetails.getUser());
    }

    // 판매자 상품 삭제 - 판매자
    @DeleteMapping("/seller/items/{itemId}")
    public ResponseStatusDto deleteItem(@PathVariable Long itemId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return itemService.deleteItem(itemId, userDetails.getUser());
    }

    // 판매자 상품 삭제 - 어드민
    @DeleteMapping("/admin/seller/items/{itemId}")
    public ResponseStatusDto deleteItemByAdmin(@PathVariable Long itemId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return itemService.deleteItemByAdmin(itemId, userDetails.getUsername());
    }

}
