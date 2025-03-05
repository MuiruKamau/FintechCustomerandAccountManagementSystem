package com.ben.app.customer;

import com.ben.app.customer.CustomerRequestDTO;
import com.ben.app.customer.CustomerResponseDTO;
import com.ben.app.customer.CustomerUpdateDTO;


import java.util.List;
import java.util.Optional;

public interface CustomerService {
    CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO);
    List<CustomerResponseDTO> getAllCustomers();
    Optional<CustomerResponseDTO> getCustomerById(Long customerId);
    Optional<CustomerResponseDTO> updateCustomer(Long customerId, CustomerUpdateDTO customerUpdateDTO);
    boolean deleteCustomer(Long customerId);
}
