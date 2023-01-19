package com.project.matchingsystem.service;

import com.project.matchingsystem.domain.Item;
import com.project.matchingsystem.domain.Transaction;
import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.dto.TransactionResponseDto;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.repository.ItemRepository;
import com.project.matchingsystem.repository.TransactionRepository;
import com.project.matchingsystem.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserProfileRepository userProfileRepository;
    private final ItemRepository itemRepository;
    private final TransactionRepository transactionRepository;

//    @Transactional(readOnly = true)
//    public List<TransactionResponseDto> getTransactionRequests(Long itemId) {
//        System.out.println("start");
//        List<Object[]> objects = transactionRepository.findByItemIdWithUserProfile(itemId);
//        System.out.println("end");
//        for (Object[] object : objects) {
//            Transaction transaction = (Transaction) object[0];
//            System.out.println("transaction.getItem() = " + transaction.getItem());
//            String nickname = (String) object[1];
//            System.out.println("nickname = " + nickname);
//        }
//        return null;
//    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionRequests(Long itemId) {
        List<Transaction> transactionList = transactionRepository.findByItemId(itemId);
        List<TransactionResponseDto> transactionResponseDtoList = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            String nickname = userProfileRepository.findByUserId(transaction.getUser().getId()).orElseThrow().getNickname();
            transactionResponseDtoList.add(new TransactionResponseDto(transaction, nickname));
        }
        return transactionResponseDtoList;
    }

    @Transactional
    public ResponseStatusDto requestTransaction(Long itemId, User user) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_ITEM.getMessage())
        );
        transactionRepository.save(new Transaction(item, user));
        return new ResponseStatusDto(HttpStatus.OK.toString(), "거래 신청 완료");
    }

    @Transactional
    public ResponseStatusDto permitTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_TRANSACTION.getMessage())
        );
        transaction.updateStatusToComplete();
        return new ResponseStatusDto(HttpStatus.OK.toString(), "거래 승인 완료");
    }

}
