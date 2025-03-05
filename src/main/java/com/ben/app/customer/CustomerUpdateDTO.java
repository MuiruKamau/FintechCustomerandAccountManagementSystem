package com.ben.app.customer;


import com.ben.app.customer.CustomerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerUpdateDTO {
    private String fullName;
    @Email(message = "Email should be valid")
    private String email;
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number should be valid")
    private String phone;
    private String profilePicture;
    private CustomerType customerType; // Added to Update DTO - Optional to include in updates
}