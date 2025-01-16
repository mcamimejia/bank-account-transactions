package com.bankTransactions.bankTransactions.service;

import lombok.RequiredArgsConstructor;
import model.TransactionAudit;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.bankTransactions.bankTransactions.repository.TransactionAuditRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionAuditService {
    private final TransactionAuditRepository repository;
    private final BankAccountService bankAccountService;

    public Flux<TransactionAudit> getUserTransactions(String userId) {
        return repository.findByUserId(userId);
    }

    public Mono<TransactionAudit> deposit(String userId, double amount) {
        return bankAccountService.getBalance(userId)
            .flatMap(initialBalance -> {
                double finalBalance = initialBalance + amount;

                // Create audit record
                TransactionAudit audit = new TransactionAudit();
                audit.setUserId(userId);
                audit.setInitialBalance(initialBalance);
                audit.setAmount(amount);
                audit.setFinalBalance(finalBalance);
                audit.setTransactionType("DEPOSIT");
                audit.setTransactionDate(LocalDateTime.now());

                return bankAccountService.updateBalance(userId, finalBalance) // Update balance via external API
                    .then(repository.save(audit)); // Save audit record
            });
    }

    public Mono<TransactionAudit> withdraw(String userId, double amount, String withdrawalType) {
        return bankAccountService.getBalance(userId)
            .flatMap(initialBalance -> {
                if (initialBalance < amount) {
                    return Mono.error(new RuntimeException("Insufficient balance"));
                }

                double finalBalance = initialBalance - amount;

                // Create audit record
                TransactionAudit audit = new TransactionAudit();
                audit.setUserId(userId);
                audit.setInitialBalance(initialBalance);
                audit.setAmount(amount);
                audit.setFinalBalance(finalBalance);
                audit.setTransactionType("WITHDRAWAL");
                audit.setWithdrawalType(withdrawalType);
                audit.setTransactionDate(LocalDateTime.now());

                return bankAccountService.updateBalance(userId, finalBalance)
                    .then(repository.save(audit));
            });
    }
}