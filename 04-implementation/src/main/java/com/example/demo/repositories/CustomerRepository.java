package com.example.demo.repositories;



import com.example.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Customer findByCustomerId(Long id);
    Customer findByEmail(String email);
    List<Customer> findAllByNameContaining(String name);
    List<Customer> findAllByAddress(String address);
    List<Customer> findAllByLateFeesGreaterThan(int fee);
    // new requirements

}
