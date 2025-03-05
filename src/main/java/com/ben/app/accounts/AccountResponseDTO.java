package com.ben.app.accounts;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountResponseDTO {
    private Long accountId;
    private Long customerId;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus status;
}
