package com.bankTransactions.bankTransactions;

import com.bankTransactions.bankTransactions.controller.TransactionAuditController;
import com.bankTransactions.bankTransactions.service.TransactionAuditService;
import model.TransactionAudit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@WebFluxTest(TransactionAuditController.class)
class TransactionAuditControllerTest {

    @Mock
    private TransactionAuditService transactionAuditService; 

    @InjectMocks
    private TransactionAuditController transactionAuditController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(transactionAuditController).build();
    }

    @Test
    void testGetUserTransactions() {
        String userId = "123";
        TransactionAudit transaction = new TransactionAudit();
        transaction.setUserId(userId);
        transaction.setInitialBalance(50.0);
        transaction.setAmount(100.0);
        transaction.setFinalBalance(150.0);
        transaction.setTransactionType("DEPOSIT");
        transaction.setTransactionDate(LocalDateTime.now());

        when(transactionAuditService.getUserTransactions(userId)).thenReturn(Flux.just(transaction));

        webTestClient.get()
            .uri("/api/transactions/{userId}", userId)
            .exchange()
            .expectStatus().isOk() 
            .expectBody()
            .jsonPath("$[0].userId").isEqualTo(userId)  
            .jsonPath("$[0].amount").isEqualTo(100.0);  
    }

    @Test
    void testDeposit() {
        String userId = "123";
        double amount = 100.0;

        TransactionAudit transaction = new TransactionAudit();
        transaction.setUserId(userId);
        transaction.setInitialBalance(50.0);
        transaction.setAmount(amount);
        transaction.setFinalBalance(150.0);
        transaction.setTransactionType("DEPOSIT");
        transaction.setTransactionDate(LocalDateTime.now());

        when(transactionAuditService.deposit(userId, amount))
                .thenReturn(Mono.just(transaction));

        webTestClient.post()
            .uri(uriBuilder -> uriBuilder.path("/api/transactions/deposit")
                    .queryParam("userId", userId)
                    .queryParam("amount", amount)
                    .build())
            .exchange()
            .expectStatus().isOk()  
            .expectBody()
            .jsonPath("$.userId").isEqualTo(userId) 
            .jsonPath("$.amount").isEqualTo(amount) 
            .jsonPath("$.finalBalance").isEqualTo(150.0); 
    }

    @Test
    void testWithdraw() {
        String userId = "123";
        double amount = 100.0;
        String withdrawalType = "ATM";

        
        TransactionAudit transaction = new TransactionAudit();
        transaction.setUserId(userId);
        transaction.setInitialBalance(150.0);
        transaction.setAmount(amount);
        transaction.setFinalBalance(50.0);
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setWithdrawalType(withdrawalType);
        transaction.setTransactionDate(LocalDateTime.now());

        when(transactionAuditService.withdraw(userId, amount, withdrawalType))
                .thenReturn(Mono.just(transaction));

        
        webTestClient.post()
            .uri(uriBuilder -> uriBuilder.path("/api/transactions/withdraw")
                    .queryParam("userId", userId)
                    .queryParam("amount", amount)
                    .queryParam("withdrawalType", withdrawalType)
                    .build())
            .exchange()
            .expectStatus().isOk()  
            .expectBody()
            .jsonPath("$.userId").isEqualTo(userId)  
            .jsonPath("$.amount").isEqualTo(amount)  
            .jsonPath("$.finalBalance").isEqualTo(50.0); 
    }

    @Test
    void testWithdrawInsufficientBalance() {
        String userId = "123";
        double amount = 200.0;
        String withdrawalType = "ATM";

        
        when(transactionAuditService.withdraw(userId, amount, withdrawalType))
                .thenReturn(Mono.error(new RuntimeException("Insufficient balance")));

        
        webTestClient.post()
            .uri(uriBuilder -> uriBuilder.path("/api/transactions/withdraw")
                    .queryParam("userId", userId)
                    .queryParam("amount", amount)
                    .queryParam("withdrawalType", withdrawalType)
                    .build())
            .exchange()
            .expectStatus().isBadRequest()  
            .expectBody()
            .jsonPath("$.message").isEqualTo("Insufficient balance"); 
    }
}