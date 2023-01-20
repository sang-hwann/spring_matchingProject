package com.project.matchingsystem.repository;

import com.project.matchingsystem.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

//    @Query("select t, p.nickname from Transaction t join fetch UserProfile p on t.user.id = p.user.id where t.item.id = :itemId")
    // 에러나서 주석처리 했습니당!
//    @Query("select t, p.nickname from Transaction t join UserProfile p on t.user.id = p.user.id where t.item.id = :itemId")
//    List<Object[]> findByItemIdWithUserProfile(@Param("itemId") Long itemId);

    List<Transaction> findByItemId(Long itemId);

}
