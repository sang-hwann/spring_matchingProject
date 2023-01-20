package com.project.matchingsystem.service;

import com.project.matchingsystem.domain.Item;
import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.dto.ItemRequestDto;
import com.project.matchingsystem.dto.ItemResponseDto;
import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.repository.CategoryRepository;
import com.project.matchingsystem.repository.ItemRepository;
import com.project.matchingsystem.repository.UserProfileRepository;
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
    private final UserProfileRepository userProfileRepository;
    private final CategoryRepository categoryRepository;

    // 특정 상품 조회
    @Transactional
    public ItemResponseDto getItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_ITEM.getMessage()));
        return new ItemResponseDto(item,item.getUser().getUsername());
    }

    // 전체 상품 조회
    @Transactional
    public Page<ItemResponseDto> getItems(Pageable pageable) {
        List<Item> itemList = itemRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<ItemResponseDto> itemResponseDto = new ArrayList<>();

        for (Item item : itemList) {
            itemResponseDto.add(new ItemResponseDto(item,item.getUser().getNickname()));
        }

        return new PageImpl<>(itemResponseDto);
    }

    @Transactional
    public Page<ItemResponseDto> getItemsByCategory(Long categoryId, Pageable pageable) {
        List<Item> itemList = itemRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId, pageable);
        List<ItemResponseDto> itemResponseDto = new ArrayList<>();

        for (Item item : itemList) {
            itemResponseDto.add(new ItemResponseDto(item,item.getUser().getNickname()));
        }

        return new PageImpl<>(itemResponseDto);
    }

    // 판매자 상품 조회
    @Transactional
    public Page<ItemResponseDto> getItemsBySeller(Long sellerId, Pageable pageable) {

        List<Item> itemList = itemRepository.findAllByUserIdOrderByCreatedAtDesc(sellerId,pageable);
        List<ItemResponseDto> itemResponseDto = new ArrayList<>();

        for (Item item : itemList) {
            itemResponseDto.add(new ItemResponseDto(item,item.getUser().getNickname()));
        }

        return new PageImpl<>(itemResponseDto);
    }

    // 상품 등록
    @Transactional
    public ResponseStatusDto uploadItem(ItemRequestDto itemRequestDto, User user) {
        // 카테고리가 존재 하는지 확인
        if (!categoryRepository.existsById(itemRequestDto.getCategoryId())) {
            throw new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage());
        }
        itemRepository.save(new Item(itemRequestDto,user));
        return new ResponseStatusDto(HttpStatus.OK.toString(),"상품 등록 완료");
    }


    // 상품 수정 - config 에서 어차피 셀러만 가능하게 해주니까 역할이 셀러인지 조건문 안 해도 되나?
    @Transactional
    public ResponseStatusDto updateItem(Long itemId, ItemRequestDto itemRequestDto, User user) {
        // 해당 상품 아이디를 가진 상품이 존재하는 지 확인하고, 있으면 수정해야 할까 -> 상품이 존재하니까 삭제를 하는 거 아닌가?

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_ITEM.getMessage()));
        if (item.getUser().getUsername().equals(user.getUsername())) {
            item.update(itemRequestDto);
            itemRepository.save(item);
        } else { throw new IllegalArgumentException(ErrorCode.AUTHORIZATION.getMessage()); }

        return new ResponseStatusDto(HttpStatus.OK.toString(),"상품 수정 완료");
    }

    //상품 삭제(판매자)
    @Transactional
    public ResponseStatusDto deleteItem(Long itemId, User user) {

        Item item = itemRepository.findById(itemId).orElseThrow(()-> new IllegalArgumentException(ErrorCode.NOT_FOUND_ITEM.getMessage()));
        if (item.getUser().getUsername().equals(user.getUsername())) {
            itemRepository.delete(item);
        }else { throw new IllegalArgumentException(ErrorCode.AUTHORIZATION.getMessage()); }

        return new ResponseStatusDto(HttpStatus.OK.toString(),"상품 삭제 완료");

    }

    //상품 삭제(관리자)
    @Transactional
    public ResponseStatusDto deleteItemByAdmin(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new IllegalArgumentException(ErrorCode.NOT_FOUND_ITEM.getMessage()));
        itemRepository.delete(item);
        return new ResponseStatusDto(HttpStatus.OK.toString(),"상품 삭제 완료");
    }

}
