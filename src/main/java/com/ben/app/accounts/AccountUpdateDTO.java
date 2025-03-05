package com.ben.app.accounts;

import lombok.Data;

@Data
public class AccountUpdateDTO {
    private AccountType accountType;
    private AccountStatus status;
}
