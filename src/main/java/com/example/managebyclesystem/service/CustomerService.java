package com.example.managebyclesystem.service;

import com.example.managebyclesystem.constants.CustomerStatus;
import com.example.managebyclesystem.model.Customer;
import com.example.managebyclesystem.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

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
        return customerRepo.findAllByOrderByCustomerNameAsc(CustomerStatus.ABLE,pageable);
    }

    public Page<Customer> getAllByOrderByNameDesc(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return customerRepo.findAllByOrderByCustomerNameDesc(CustomerStatus.ABLE,pageable);
    }

    public Page<Customer> getAllByOrderByRewardPointAsc(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return customerRepo.findAllByOrderByRewardPointsAsc(CustomerStatus.ABLE,pageable);
    }

    public Page<Customer> getAllByOrderByRewardPointDesc(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return customerRepo.findAllByOrderByRewardPointsDesc(CustomerStatus.ABLE,pageable);
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

    public Customer updateCustomer(int id, Customer newCustomerData) {
        Customer existing = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        boolean changed = false;


        if (newCustomerData.getCustomerName() != null
                && !Objects.equals(existing.getCustomerName(), newCustomerData.getCustomerName())) {
            existing.setCustomerName(newCustomerData.getCustomerName());
            changed = true;
        }

        if (newCustomerData.getCustomerPhone() != null
                && !Objects.equals(existing.getCustomerPhone(), newCustomerData.getCustomerPhone())) {
            if (customerRepo.existsByCustomerPhone(newCustomerData.getCustomerPhone())) {
                throw new IllegalArgumentException("Số điện thoại đã tồn tại");
            }
            existing.setCustomerPhone(newCustomerData.getCustomerPhone());
            changed = true;
        }

        if (newCustomerData.getCustomerEmail() != null
                && !Objects.equals(existing.getCustomerEmail(), newCustomerData.getCustomerEmail())) {
            if (customerRepo.existsByCustomerEmail(newCustomerData.getCustomerEmail())) {
                throw new IllegalArgumentException("Email đã tồn tại");
            }
            existing.setCustomerEmail(newCustomerData.getCustomerEmail());
            changed = true;
        }

        if (newCustomerData.getRewardPoints() != 0
                && existing.getRewardPoints() != newCustomerData.getRewardPoints()) {
            existing.setRewardPoints(newCustomerData.getRewardPoints());
            changed = true;
        }

        if (newCustomerData.getCardType() != null
                && !Objects.equals(existing.getCardType(), newCustomerData.getCardType())) {
            existing.setCardType(newCustomerData.getCardType());
            changed = true;
        }

        return changed ? customerRepo.save(existing) : existing;
    }
    public Optional<Customer> getCustomerById(int id) {
        return customerRepo.findById(id);
    }
    public Page<Customer> getCustomerByName(String name, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return customerRepo.searchCustomers(name, CustomerStatus.ABLE, pageable);
    }

    public Page<Customer> getCustomerByEmail(String email, int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return  customerRepo.searchCustomers(email,CustomerStatus.ABLE,pageable);
    }

    public Page<Customer> getCustomerByCardType(String cardType, int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return  customerRepo.searchCustomers(cardType, CustomerStatus.ABLE, pageable);
    }

    public void deleteCustomer(int id){
        Customer existing = customerRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng id: " + id));
        existing.setStatus(CustomerStatus.DISABLE);
        customerRepo.save(existing);

    }


}
