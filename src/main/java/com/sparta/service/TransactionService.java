package com.sparta.service;

import com.sparta.dto.ResponseStatusDto;
import com.sparta.dto.TransactionResponseDto;
import com.sparta.repository.ItemRepository;
import com.sparta.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final ItemRepository itemRepository;
    private final TransactionRepository transactionRepository;

    public Page<TransactionResponseDto> getTransactions(Long itemId) {
        return null;
    }

    public ResponseStatusDto requestTransaction(Long itemId) {
        return null;
    }

    public ResponseStatusDto permitTransaction(Long transactionId) {
        return null;
    }

}
