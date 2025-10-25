package com.example.managebyclesystem.service;

import com.example.managebyclesystem.model.RentalOrder;
import com.example.managebyclesystem.model.RentalOrder.ActiveStatus;
import com.example.managebyclesystem.model.RentalOrder.RentalStatus;
import com.example.managebyclesystem.repository.RentalOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class RentalOrderService {

    private final RentalOrderRepository rentalOrderRepository;

    @Autowired
    public RentalOrderService(RentalOrderRepository rentalOrderRepository) {
        this.rentalOrderRepository = rentalOrderRepository;
    }

    // ---------------- READ ----------------
    public Page<RentalOrder> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return rentalOrderRepository.findByActiveStatus(ActiveStatus.ABLE, pageable);
    }

    public Optional<RentalOrder> getOrderById(int id) {
        return rentalOrderRepository.findById(id);
    }

    // ---------------- SORT ----------------
    public Page<RentalOrder> getAllByOrderByRentalDateAsc(int page, int size) {
        return rentalOrderRepository.findAllByOrderByRentalDateAsc(PageRequest.of(page, size));
    }

    public Page<RentalOrder> getAllByOrderByRentalDateDesc(int page, int size) {
        return rentalOrderRepository.findAllByOrderByRentalDateDesc(PageRequest.of(page, size));
    }

    public Page<RentalOrder> getAllByOrderByTotalAmountAsc(int page, int size) {
        return rentalOrderRepository.findAllByOrderByTotalAmountAsc(PageRequest.of(page, size));
    }

    public Page<RentalOrder> getAllByOrderByTotalAmountDesc(int page, int size) {
        return rentalOrderRepository.findAllByOrderByTotalAmountDesc(PageRequest.of(page, size));
    }
}