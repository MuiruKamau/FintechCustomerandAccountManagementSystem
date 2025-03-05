package com.ben.app.transactions;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO);
    List<TransactionResponseDTO> getAllTransactions();
    Optional<TransactionResponseDTO> getTransactionById(Long transactionId);
    Optional<TransactionResponseDTO> updateTransaction(Long transactionId, TransactionUpdateDTO transactionUpdateDTO);
    boolean deleteTransaction(Long transactionId);
}