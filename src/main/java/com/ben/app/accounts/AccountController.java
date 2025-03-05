package com.ben.app.accounts;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO) {
        AccountResponseDTO createdAccount = accountService.createAccount(accountRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Account created successfully",
                "status", HttpStatus.CREATED.value(),
                "data", createdAccount
        ));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAccounts() {
        List<AccountResponseDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(Map.of(
                "message", "Accounts retrieved successfully",
                "status", HttpStatus.OK.value(),
                "data", accounts
        ));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Map<String, Object>> getAccountById(@PathVariable Long accountId) {
        Optional<AccountResponseDTO> account = accountService.getAccountById(accountId);
        return account.map(accountResponseDTO -> ResponseEntity.ok(Map.of(
                "message", "Account retrieved successfully",
                "status", HttpStatus.OK.value(),
                "data", accountResponseDTO
        ))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", "Account not found",
                "status", HttpStatus.NOT_FOUND.value()
        )));
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Map<String, Object>> updateAccount(@PathVariable Long accountId, @Valid @RequestBody AccountUpdateDTO accountUpdateDTO) {
        Optional<AccountResponseDTO> updatedAccount = accountService.updateAccount(accountId, accountUpdateDTO);
        return updatedAccount.map(accountResponseDTO -> ResponseEntity.ok(Map.of(
                "message", "Account updated successfully",
                "status", HttpStatus.OK.value(),
                "data", updatedAccount
        ))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", "Account not found",
                "status", HttpStatus.NOT_FOUND.value()
        )));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Map<String, Object>> deleteAccount(@PathVariable Long accountId) {
        boolean deleted = accountService.deleteAccount(accountId);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Account not found",
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }
}
