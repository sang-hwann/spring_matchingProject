package com.project.matchingsystem.repository;

import com.project.matchingsystem.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ItemRepository extends JpaRepository<Item, Long> {
}
