package com.bankTransactions.bankTransactions.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BankAccountService {
    private final WebClient webClient;

    public BankAccountService(WebClient.Builder builder, @Value("${external.api.base-url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public Mono<Double> getBalance(String userId) {
        return webClient.get()
            .uri("/balance/{userId}", userId)
            .retrieve()
            .bodyToMono(Double.class);
    }

    public Mono<Void> updateBalance(String userId, double newBalance) {
        return webClient.post()
            .uri("/update-balance")
            .bodyValue(new UpdateBalanceRequest(userId, newBalance))
            .retrieve()
            .bodyToMono(Void.class);
    }

    private record UpdateBalanceRequest(String userId, double newBalance) {}
}