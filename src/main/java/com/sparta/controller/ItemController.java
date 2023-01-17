package com.sparta.controller;

import com.sparta.dto.ItemRequestDto;
import com.sparta.dto.ResponseStatusDto;
import com.sparta.security.UserDetailsImpl;
import com.sparta.service.ItemService;
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

    @GetMapping("/items/{itemId}")
    public ItemRequestDto getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping("/seller/{sellerId}/items")
    public List<ItemRequestDto> getItemsBySeller(@PathVariable Long sellerId, Pageable pageable) {
        return itemService.getItemsBySeller(sellerId, pageable).getContent();
    }

    @PostMapping("/seller/items")
    public ResponseStatusDto uploadItem(@Validated @RequestBody ItemRequestDto itemRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return itemService.uploadItem(itemRequestDto, userDetails.getUsername());
    }

    @PutMapping("/seller/items/{itemId}")
    public ResponseStatusDto updateItem(@PathVariable Long itemId, @Validated @RequestBody ItemRequestDto itemRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return itemService.updateItem(itemId, itemRequestDto, userDetails.getUsername());
    }

    @DeleteMapping("/seller/items/{itemId}")
    public ResponseStatusDto deleteItem(@PathVariable Long itemId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return itemService.deleteItem(itemId, userDetails.getUsername());
    }

    @DeleteMapping("/admin/seller/items/{itemId}")
    public ResponseStatusDto deleteItemByAdmin(@PathVariable Long itemId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return itemService.deleteItemByAdmin(itemId, userDetails.getUsername());
    }

}
