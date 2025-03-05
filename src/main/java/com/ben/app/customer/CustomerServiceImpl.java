package com.ben.app.customer;

import com.ben.app.customer.CustomerRequestDTO;
import com.ben.app.customer.CustomerResponseDTO;
import com.ben.app.customer.CustomerUpdateDTO;
import com.ben.app.customer.CustomerModel;
import com.ben.app.customer.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
        CustomerModel customer = new CustomerModel(); // Renamed Customer to CustomerModel
        customer.setFullName(customerRequestDTO.getFullName());
        customer.setEmail(customerRequestDTO.getEmail());
        customer.setPhone(customerRequestDTO.getPhone());
        customer.setDateOfRegistration(customerRequestDTO.getDateOfRegistration());
        customer.setCustomerType(customerRequestDTO.getCustomerType()); // Enum is directly set
        customer.setProfilePicture(customerRequestDTO.getProfilePicture());

        CustomerModel savedCustomer = customerRepository.save(customer); // Renamed Customer to CustomerModel
        return mapToDto(savedCustomer);
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        List<CustomerModel> customers = customerRepository.findAll(); // Renamed Customer to CustomerModel
        return customers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerResponseDTO> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .map(this::mapToDto);
    }

    @Override
    public Optional<CustomerResponseDTO> updateCustomer(Long customerId, CustomerUpdateDTO customerUpdateDTO) {
        return customerRepository.findById(customerId)
                .map(existingCustomer -> {
                    if (customerUpdateDTO.getFullName() != null) {
                        existingCustomer.setFullName(customerUpdateDTO.getFullName());
                    }
                    if (customerUpdateDTO.getEmail() != null) {
                        existingCustomer.setEmail(customerUpdateDTO.getEmail());
                    }
                    if (customerUpdateDTO.getPhone() != null) {
                        existingCustomer.setPhone(customerUpdateDTO.getPhone());
                    }
                    if (customerUpdateDTO.getProfilePicture() != null) {
                        existingCustomer.setProfilePicture(customerUpdateDTO.getProfilePicture());
                    }
                    if (customerUpdateDTO.getCustomerType() != null) { // Allow updating CustomerType
                        existingCustomer.setCustomerType(customerUpdateDTO.getCustomerType());
                    }
                    CustomerModel updatedCustomer = customerRepository.save(existingCustomer); // Renamed Customer to CustomerModel
                    return mapToDto(updatedCustomer);
                });
    }

    @Override
    public boolean deleteCustomer(Long customerId) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }

    private CustomerResponseDTO mapToDto(CustomerModel customer) { // Renamed Customer to CustomerModel in parameter type
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setFullName(customer.getFullName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setDateOfRegistration(customer.getDateOfRegistration());
        dto.setCustomerType(customer.getCustomerType()); // Enum is directly mapped
        dto.setProfilePicture(customer.getProfilePicture());
        return dto;
    }
}