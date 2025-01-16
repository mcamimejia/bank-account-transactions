package model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "transaction_audits")
public class TransactionAudit {
    @Id
    private String id;
    private String userId;
    private double initialBalance;
    private double amount;
    private double finalBalance;
    private String transactionType;
    private LocalDateTime transactionDate;
    private String withdrawalType;
    
}