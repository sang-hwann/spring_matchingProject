package com.project.matchingsystem.service;

import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.dto.TransactionResponseDto;
import com.project.matchingsystem.repository.ItemRepository;
import com.project.matchingsystem.repository.TransactionRepository;
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
