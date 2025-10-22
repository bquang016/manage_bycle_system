package com.example.managebyclesystem.service;

import com.example.managebyclesystem.constants.CustomerStatus;
import com.example.managebyclesystem.model.Customer;
import com.example.managebyclesystem.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepo customerRepo;

    private static final int PAGE_SIZE = 10;

    @Autowired
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Page<Customer> getAllCustomer(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return customerRepo.findByStatus(CustomerStatus.ABLE, pageable);
    }
}
