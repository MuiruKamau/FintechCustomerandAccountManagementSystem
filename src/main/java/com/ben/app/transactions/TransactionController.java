package com.ben.app.transactions;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTransaction(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        TransactionResponseDTO createdTransaction = transactionService.createTransaction(transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Transaction recorded successfully",
                "status", HttpStatus.CREATED.value(),
                "data", createdTransaction
        ));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTransactions() {
        List<TransactionResponseDTO> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(Map.of(
                "message", "Transactions retrieved successfully",
                "status", HttpStatus.OK.value(),
                "data", transactions
        ));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Map<String, Object>> getTransactionById(@PathVariable Long transactionId) {
        Optional<TransactionResponseDTO> transaction = transactionService.getTransactionById(transactionId);
        return transaction.map(transactionResponseDTO -> ResponseEntity.ok(Map.of(
                "message", "Transaction retrieved successfully",
                "status", HttpStatus.OK.value(),
                "data", transactionResponseDTO
        ))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", "Transaction not found",
                "status", HttpStatus.NOT_FOUND.value()
        )));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Map<String, Object>> updateTransaction(@PathVariable Long transactionId, @Valid @RequestBody TransactionUpdateDTO transactionUpdateDTO) {
        Optional<TransactionResponseDTO> updatedTransaction = transactionService.updateTransaction(transactionId, transactionUpdateDTO);
        return updatedTransaction.map(transactionResponseDTO -> ResponseEntity.ok(Map.of(
                "message", "Transaction updated successfully",
                "status", HttpStatus.OK.value(),
                "data", updatedTransaction
        ))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", "Transaction not found",
                "status", HttpStatus.NOT_FOUND.value()
        )));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Map<String, Object>> deleteTransaction(@PathVariable Long transactionId) {
        boolean deleted = transactionService.deleteTransaction(transactionId);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Transaction not found",
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }
}