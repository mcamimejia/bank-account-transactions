package com.bankTransactions.bankTransactions.controller;

import lombok.RequiredArgsConstructor;
import model.TransactionAudit;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.bankTransactions.bankTransactions.service.TransactionAuditService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionAuditController {
    private final TransactionAuditService service;

    @GetMapping(value = "/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TransactionAudit> getUserTransactions(@PathVariable String userId) {
        return service.getUserTransactions(userId);
    }

    @PostMapping("/deposit")
    public Mono<TransactionAudit> deposit(@RequestParam String userId, @RequestParam double amount) {
        return service.deposit(userId, amount);
    }

    @PostMapping("/withdraw")
    public Mono<TransactionAudit> withdraw(@RequestParam String userId, @RequestParam double amount, @RequestParam String withdrawalType) {
        return service.withdraw(userId, amount, withdrawalType);
    }
}
