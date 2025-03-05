package com.ben.app.transactions;

import com.ben.app.accounts.AccountModel;
import com.ben.app.accounts.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional // Use Transactional for methods that modify balance
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO) {
        // 1. Fetch Account
        AccountModel account = accountRepository.findById(transactionRequestDTO.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + transactionRequestDTO.getAccountId()));

        // 2. Create Transaction Model
        TransactionModel transaction = new TransactionModel();
        transaction.setAccount(account);
        transaction.setTransactionType(transactionRequestDTO.getTransactionType());
        transaction.setAmount(transactionRequestDTO.getAmount());
        transaction.setStatus(transactionRequestDTO.getStatus());

        // 3. Save Transaction (important to save first to get transaction ID if needed later for reversal)
        TransactionModel savedTransaction = transactionRepository.save(transaction);

        // 4. Update Account Balance if status is APPROVED
        if (transactionRequestDTO.getStatus() == TransactionStatus.APPROVED) {
            updateAccountBalance(transaction);
        }

        // 5. Return Response DTO
        return mapToDto(savedTransaction);
    }

    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TransactionResponseDTO> getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(this::mapToDto);
    }

    @Override
    @Transactional // Use Transactional for methods that modify balance
    public Optional<TransactionResponseDTO> updateTransaction(Long transactionId, TransactionUpdateDTO transactionUpdateDTO) {
        return transactionRepository.findById(transactionId)
                .map(existingTransaction -> {
                    TransactionStatus oldStatus = existingTransaction.getStatus(); // Get the old status
                    BigDecimal oldAmount = existingTransaction.getAmount();
                    TransactionType oldType = existingTransaction.getTransactionType();

                    // Update fields from DTO
                    if (transactionUpdateDTO.getStatus() != null) {
                        existingTransaction.setStatus(transactionUpdateDTO.getStatus());
                    }
                    if (transactionUpdateDTO.getAmount() != null) {
                        existingTransaction.setAmount(transactionUpdateDTO.getAmount());
                    }
                    if (transactionUpdateDTO.getTransactionType() != null) {
                        existingTransaction.setTransactionType(transactionUpdateDTO.getTransactionType());
                    }

                    TransactionModel updatedTransaction = transactionRepository.save(existingTransaction);

                    // Handle Balance Updates based on Status Change
                    if (oldStatus != updatedTransaction.getStatus()) {
                        if (updatedTransaction.getStatus() == TransactionStatus.APPROVED) {
                            updateAccountBalance(updatedTransaction); // Approve: Update balance
                        } else if (oldStatus == TransactionStatus.APPROVED) {
                            reverseAccountBalanceUpdate(updatedTransaction); // Revert: Reverse balance update if status changed from APPROVED
                        }
                    } else if (updatedTransaction.getStatus() == TransactionStatus.APPROVED && (!oldAmount.equals(updatedTransaction.getAmount()) || oldType != updatedTransaction.getTransactionType())) {
                        // If status is still APPROVED but amount or type changed, we need to adjust balance
                        reverseAccountBalanceUpdate(existingTransaction); // Reverse based on old values
                        updateAccountBalance(updatedTransaction);        // Apply balance update with new values
                    }

                    return mapToDto(updatedTransaction);
                });
    }

    @Override
    @Transactional // Use Transactional for methods that modify balance
    public boolean deleteTransaction(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(transaction -> {
                    if (transaction.getStatus() == TransactionStatus.APPROVED) {
                        reverseAccountBalanceUpdate(transaction); // Reverse balance update before deleting
                    }
                    transactionRepository.deleteById(transactionId);
                    return true;
                }).orElse(false);
    }

    private TransactionResponseDTO mapToDto(TransactionModel transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setAccountId(transaction.getAccount().getAccountId());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setAmount(transaction.getAmount());
        dto.setStatus(transaction.getStatus());
        return dto;
    }

    private void updateAccountBalance(TransactionModel transaction) {
        AccountModel account = transaction.getAccount();
        BigDecimal transactionAmount = transaction.getAmount();
        TransactionType transactionType = transaction.getTransactionType();

        switch (transactionType) {
            case DEPOSIT:
                account.setBalance(account.getBalance().add(transactionAmount));
                break;
            case WITHDRAWAL:
                // **Crucial Validation:** Check for sufficient balance before withdrawal
                if (account.getBalance().compareTo(transactionAmount) < 0) {
                    throw new IllegalStateException("Insufficient funds for withdrawal from Account ID: " + account.getAccountId());
                }
                account.setBalance(account.getBalance().subtract(transactionAmount));
                break;
            case TRANSFER:

                break;
        }
        accountRepository.save(account); // Save the updated account balance
    }

    private void reverseAccountBalanceUpdate(TransactionModel transaction) {
        AccountModel account = transaction.getAccount();
        BigDecimal transactionAmount = transaction.getAmount();
        TransactionType transactionType = transaction.getTransactionType();

        switch (transactionType) {
            case DEPOSIT:
                account.setBalance(account.getBalance().subtract(transactionAmount)); // Reverse deposit: subtract
                break;
            case WITHDRAWAL:
                account.setBalance(account.getBalance().add(transactionAmount));    // Reverse withdrawal: add back
                break;
            case TRANSFER:
                // Reverse logic for TRANSFER if implemented
                break;
        }
        accountRepository.save(account); // Save the updated (reversed) account balance
    }
}