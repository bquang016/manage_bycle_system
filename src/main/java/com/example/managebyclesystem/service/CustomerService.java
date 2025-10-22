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
    public Page<Customer> getAllByOrderByNameAsc(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return customerRepo.findAllByOrderByCustomerNameAsc(pageable);
    }

    public Page<Customer> getAllByOrderByNameDesc(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return customerRepo.findAllByOrderByCustomerNameDesc(pageable);
    }

    public Page<Customer> getAllByOrderByRewardPointAsc(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return customerRepo.findAllByOrderByRewardPointsAsc(pageable);
    }

    public Page<Customer> getAllByOrderByRewardPointDesc(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return customerRepo.findAllByOrderByRewardPointsDesc(pageable);
    }

    public Customer addCustomer(Customer customer) {

        if (customer.getCustomerPhone() == null || customer.getCustomerPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }
        if (customerRepo.existsByCustomerPhone(customer.getCustomerPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        if (customer.getCustomerEmail() == null || customer.getCustomerEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        if (!customer.getCustomerEmail().contains("@")) {
            throw new IllegalArgumentException("Email phải có ký tự '@'");
        }
        if (customerRepo.existsByCustomerEmail(customer.getCustomerEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        return customerRepo.save(customer);
    }


}
