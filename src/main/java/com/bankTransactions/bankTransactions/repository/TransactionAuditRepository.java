package com.bankTransactions.bankTransactions.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import model.TransactionAudit;
import reactor.core.publisher.Flux;

public interface TransactionAuditRepository extends ReactiveMongoRepository<TransactionAudit, String> {
	
	@Tailable
    Flux<TransactionAudit> findByUserId(String userId);
}
