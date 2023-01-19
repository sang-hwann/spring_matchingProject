package com.project.matchingsystem.repository;

import com.project.matchingsystem.domain.SellerManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerManagementRepository extends JpaRepository<SellerManagement, Long> {

    Optional<SellerManagement> findById(Long userId);
    Optional<SellerManagement> findByUserId(Long userId);
}
