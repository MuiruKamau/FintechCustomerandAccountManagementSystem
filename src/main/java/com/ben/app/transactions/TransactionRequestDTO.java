package com.ben.app.transactions;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionRequestDTO {

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    @DecimalMin(value = "0.01", message = "Transaction amount must be greater than zero")
    @NotNull(message = "Transaction amount is required")
    private BigDecimal amount;

    @NotNull(message = "Transaction status is required")
    private TransactionStatus status; // Initially could be PENDING
}