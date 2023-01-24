package com.project.matchingsystem.repository;

import com.project.matchingsystem.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t from Transaction t join fetch t.user where t.item.id = :itemId")
    List<Transaction> findByItemId(@Param("itemId") Long itemId);

}
