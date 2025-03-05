package com.ben.app.customer;



import com.ben.app.customer.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Optional, but good practice to explicitly mark it as a repository
public interface CustomerRepository extends JpaRepository<CustomerModel, Long> {

}