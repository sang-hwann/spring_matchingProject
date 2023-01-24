package com.project.matchingsystem.repository;

import com.project.matchingsystem.domain.Category;
import com.project.matchingsystem.domain.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChattingRepository  extends JpaRepository<Chatting, Long> {

    boolean existsByUserNameAndSellerName(String userName, String sellerName);

}
