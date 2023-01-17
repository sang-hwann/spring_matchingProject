package com.sparta.service;

import com.sparta.dto.ItemRequestDto;
import com.sparta.dto.ResponseStatusDto;
import com.sparta.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemRequestDto getItem(Long itemId) {
        return null;
    }

    public Page<ItemRequestDto> getItemsBySeller(Long sellerId, Pageable pageable) {
        return null;
    }

    public ResponseStatusDto uploadItem(ItemRequestDto itemRequestDto, String username) {
        return null;
    }

    public ResponseStatusDto updateItem(Long itemId, ItemRequestDto itemRequestDto, String username) {
        return null;
    }

    public ResponseStatusDto deleteItem(Long itemId, String username) {
        return null;
    }

    public ResponseStatusDto deleteItemByAdmin(Long itemId, String username) {
        return null;
    }

}
