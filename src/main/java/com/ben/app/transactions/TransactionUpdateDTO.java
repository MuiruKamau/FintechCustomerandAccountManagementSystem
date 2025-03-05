package com.ben.app.transactions;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionUpdateDTO {
    private TransactionStatus status;
    private BigDecimal amount; // Allow amount update as well
    private TransactionType transactionType; // Allow type update as well
}