package com.ben.app.accounts;

import com.ben.app.customer.CustomerModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "accounts")
public class AccountModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY) // Many accounts belong to one customer
    @JoinColumn(name = "customer_id", nullable = false) // Foreign key column in accounts table
    private CustomerModel customer;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @DecimalMin(value = "0.00", message = "Initial balance must be non-negative")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
