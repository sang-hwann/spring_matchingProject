package com.sparta.matchingsystem.repository;

import com.sparta.matchingsystem.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
