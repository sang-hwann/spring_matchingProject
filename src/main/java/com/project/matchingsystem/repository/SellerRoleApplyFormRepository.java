package com.project.matchingsystem.repository;

import com.project.matchingsystem.domain.SellerRoleApplyForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRoleApplyFormRepository extends JpaRepository<SellerRoleApplyForm, Long> {
}
