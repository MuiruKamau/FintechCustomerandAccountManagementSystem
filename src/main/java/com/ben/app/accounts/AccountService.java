package com.ben.app.accounts;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO);
    List<AccountResponseDTO> getAllAccounts();
    Optional<AccountResponseDTO> getAccountById(Long accountId);
    Optional<AccountResponseDTO> updateAccount(Long accountId, AccountUpdateDTO accountUpdateDTO);
    boolean deleteAccount(Long accountId);
}
