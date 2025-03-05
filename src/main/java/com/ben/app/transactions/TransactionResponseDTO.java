package com.ben.app.transactions;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionResponseDTO {
    private Long transactionId;
    private Long accountId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private TransactionStatus status;
}