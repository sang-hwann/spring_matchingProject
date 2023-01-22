package com.project.matchingsystem.controller;

import com.project.matchingsystem.dto.response.ResponseStatusDto;
import com.project.matchingsystem.dto.response.TransactionResponseDto;
import com.project.matchingsystem.security.UserDetailsImpl;
import com.project.matchingsystem.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/items/{itemId}/transactions")
    public List<TransactionResponseDto> getTransactions(@PathVariable Long itemId) {
        return transactionService.getTransactionRequests(itemId);
    }

    @PostMapping("/items/{itemId}/transactions")
    public ResponseStatusDto requestTransaction(@PathVariable Long itemId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return transactionService.requestTransaction(itemId, userDetails.getUser());
    }

    @PostMapping("/transactions/{transactionId}")
    public ResponseStatusDto permitTransaction(@PathVariable Long transactionId) {
        return transactionService.permitTransaction(transactionId);
    }

}
