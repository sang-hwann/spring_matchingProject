package com.project.matchingsystem.repository;

import com.project.matchingsystem.domain.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findById(Long id);

    void deleteById(Long id);

    // 전체 판매 상품 조회
    List<Item> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 판매자 상품 조회
    List<Item> findAllByUserIdOrderByCreatedAtDesc(Long sellerId, Pageable pageable);

    // 카테고리별 상품 조회
    List<Item> findByCategoryIdOrderByCreatedAtDesc(Long categoryId, Pageable pageable);

}
