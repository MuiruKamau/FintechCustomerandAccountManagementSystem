package com.ben.app.accounts;

import com.ben.app.customer.CustomerModel;
import com.ben.app.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository; // Need CustomerRepository to fetch Customer

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        // 1. Fetch the Customer
        CustomerModel customer = customerRepository.findById(accountRequestDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + accountRequestDTO.getCustomerId())); // Or handle with a custom exception

        // 2. Create AccountModel from DTO
        AccountModel account = new AccountModel();
        account.setCustomer(customer);
        account.setAccountType(accountRequestDTO.getAccountType());
        account.setBalance(accountRequestDTO.getInitialBalance());
        account.setStatus(accountRequestDTO.getStatus());

        // 3. Save the Account
        AccountModel savedAccount = accountRepository.save(account);

        // 4. Convert and return Response DTO
        return mapToDto(savedAccount);
    }

    @Override
    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AccountResponseDTO> getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .map(this::mapToDto);
    }

    @Override
    public Optional<AccountResponseDTO> updateAccount(Long accountId, AccountUpdateDTO accountUpdateDTO) {
        return accountRepository.findById(accountId)
                .map(existingAccount -> {
                    if (accountUpdateDTO.getAccountType() != null) {
                        existingAccount.setAccountType(accountUpdateDTO.getAccountType());
                    }
                    if (accountUpdateDTO.getStatus() != null) {
                        existingAccount.setStatus(accountUpdateDTO.getStatus());
                    }
                    AccountModel updatedAccount = accountRepository.save(existingAccount);
                    return mapToDto(updatedAccount);
                });
    }

    @Override
    public boolean deleteAccount(Long accountId) {
        if (accountRepository.existsById(accountId)) {
            accountRepository.deleteById(accountId);
            return true;
        }
        return false;
    }

    private AccountResponseDTO mapToDto(AccountModel account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountId(account.getAccountId());
        dto.setCustomerId(account.getCustomer().getCustomerId()); // Get Customer ID from related CustomerModel
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setStatus(account.getStatus());
        return dto;
    }
}
