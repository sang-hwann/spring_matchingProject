package com.project.matchingsystem.service;

import com.project.matchingsystem.domain.Item;
import com.project.matchingsystem.domain.Transaction;
import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.dto.TransactionResponseDto;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.repository.ItemRepository;
import com.project.matchingsystem.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final ItemRepository itemRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionRequests(Long itemId) {
        List<Transaction> transactionList = transactionRepository.findByItemId(itemId);
        List<TransactionResponseDto> transactionResponseDtoList = new ArrayList<>();
        transactionList.forEach(transaction -> {
            transactionResponseDtoList.add(new TransactionResponseDto(transaction, transaction.getUser().getNickname()));
        });
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

        transactionRepository.findByItemId(transaction.getItem().getId()).forEach(Transaction::updateStatusToCancel);
        transaction.updateStatusToComplete();

        return new ResponseStatusDto(HttpStatus.OK.toString(), "거래 승인 완료");
    }

}
