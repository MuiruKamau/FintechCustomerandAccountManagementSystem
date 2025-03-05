package com.ben.app.customer;


import com.ben.app.customer.CustomerRequestDTO;
import com.ben.app.customer.CustomerResponseDTO;
import com.ben.app.customer.CustomerUpdateDTO;
import com.ben.app.customer.CustomerModel; // Renamed import from Customer to CustomerModel
import com.ben.app.customer.CustomerRepository;
import com.ben.app.customer.CustomerService;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

    @RestController
    @RequestMapping("/api/customers")
    public class CustomerController {

        private final CustomerService customerService;

        @Autowired
        public CustomerController(CustomerService customerService) {
            this.customerService = customerService;
        }

        @PostMapping
        public ResponseEntity<Map<String, Object>> createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
            CustomerResponseDTO createdCustomer = customerService.createCustomer(customerRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Customer created successfully",
                    "status", HttpStatus.CREATED.value(),
                    "data", createdCustomer
            ));
        }

        @GetMapping
        public ResponseEntity<Map<String, Object>> getAllCustomers() {
            List<CustomerResponseDTO> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(Map.of(
                    "message", "Customers retrieved successfully",
                    "status", HttpStatus.OK.value(),
                    "data", customers
            ));
        }

        @GetMapping("/{customerId}")
        public ResponseEntity<Map<String, Object>> getCustomerById(@PathVariable Long customerId) {
            Optional<CustomerResponseDTO> customer = customerService.getCustomerById(customerId);
            return customer.map(customerResponseDTO -> ResponseEntity.ok(Map.of(
                    "message", "Customer retrieved successfully",
                    "status", HttpStatus.OK.value(),
                    "data", customerResponseDTO
            ))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Customer not found",
                    "status", HttpStatus.NOT_FOUND.value()
            )));
        }

        @PutMapping("/{customerId}")
        public ResponseEntity<Map<String, Object>> updateCustomer(@PathVariable Long customerId, @Valid @RequestBody CustomerUpdateDTO customerUpdateDTO) {
            Optional<CustomerResponseDTO> updatedCustomer = customerService.updateCustomer(customerId, customerUpdateDTO);
            return updatedCustomer.map(customerResponseDTO -> ResponseEntity.ok(Map.of(
                    "message", "Customer updated successfully",
                    "status", HttpStatus.OK.value(),
                    "data", customerResponseDTO
            ))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Customer not found",
                    "status", HttpStatus.NOT_FOUND.value()
            )));
        }

        @DeleteMapping("/{customerId}")
        public ResponseEntity<Map<String, Object>> deleteCustomer(@PathVariable Long customerId) {
            boolean deleted = customerService.deleteCustomer(customerId);
            if (deleted) {
                return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "message", "Customer not found",
                        "status", HttpStatus.NOT_FOUND.value()
                ));
            }
        }
    }