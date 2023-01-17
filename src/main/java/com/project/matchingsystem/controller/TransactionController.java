package com.project.matchingsystem.controller;

import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.dto.TransactionResponseDto;
import com.project.matchingsystem.service.TransactionService;
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
