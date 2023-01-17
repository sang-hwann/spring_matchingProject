package com.sparta.matchingsystem.repository;

import com.sparta.matchingsystem.domain.SellerRoleApplyForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRoleApplyFormRepository extends JpaRepository<SellerRoleApplyForm, Long> {
}
