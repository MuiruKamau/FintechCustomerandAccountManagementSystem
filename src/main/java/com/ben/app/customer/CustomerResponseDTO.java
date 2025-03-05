package com.ben.app.customer;

import com.ben.app.customer.CustomerType;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CustomerResponseDTO {
    private Long customerId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfRegistration;
    private CustomerType customerType; // Changed to enum
    private String profilePicture;
}