package com.sparta.matchingsystem.controller;

import com.sparta.matchingsystem.dto.ResponseStatusDto;
import com.sparta.matchingsystem.dto.TransactionResponseDto;
import com.sparta.matchingsystem.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/seller/items/{itemId}/transactions")
    public List<TransactionResponseDto> getTransactions(@PathVariable Long itemId) {
        return transactionService.getTransactions(itemId).getContent();
    }

    @PostMapping("/items/{itemId}/transactions")
    public ResponseStatusDto requestTransaction(@PathVariable Long itemId) {
        return transactionService.requestTransaction(itemId);
    }

    @PostMapping("/seller/transactions/{transactionId}")
    public ResponseStatusDto permitTransaction(@PathVariable Long transactionId) {
        return transactionService.permitTransaction(transactionId);
    }

}
