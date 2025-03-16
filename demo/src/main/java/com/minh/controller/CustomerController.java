package com.minh.controller;

import com.minh.entity.Customer;
import com.minh.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/customer")
    public ResponseEntity<?> getCustomer(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return ResponseEntity.ok().body(customerPage);
    }
}
