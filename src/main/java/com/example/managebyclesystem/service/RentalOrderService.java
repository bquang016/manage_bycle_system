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

    public Page<RentalOrder> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return rentalOrderRepository.findByActiveStatus(ActiveStatus.ABLE, pageable);
    }

    public Optional<RentalOrder> getOrderById(int id) {
        return rentalOrderRepository.findById(id);
    }

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
    public void addRentalOrder(RentalOrder rentalOrder) {
        validateRentalOrder(rentalOrder);
        rentalOrder.setRentalOrderActiveStatus(ActiveStatus.ABLE);
        rentalOrderRepository.save(rentalOrder);
    }

    public void updateRentalOrder(RentalOrder updatedRentalOrder) {
        if (updatedRentalOrder.getRentalOrderId() == 0) {
            throw new IllegalArgumentException("ID đơn thuê không hợp lệ để cập nhật");
        }

        RentalOrder existing = rentalOrderRepository.findById(updatedRentalOrder.getRentalOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn thuê có ID: " + updatedRentalOrder.getRentalOrderId()));

        validateRentalOrder(updatedRentalOrder);

        existing.setRentalOrderCustomerId(updatedRentalOrder.getRentalOrderCustomerId().trim());
        existing.setRentalOrderBikeId(updatedRentalOrder.getRentalOrderBikeId().trim());
        existing.setRentalOrderRentalDate(updatedRentalOrder.getRentalOrderRentalDate());
        existing.setRentalOrderRentalTime(updatedRentalOrder.getRentalOrderRentalTime());
        existing.setRentalOrderTotalAmount(updatedRentalOrder.getRentalOrderTotalAmount());
        existing.setRentalOrderStatus(updatedRentalOrder.getRentalOrderStatus());

        rentalOrderRepository.save(existing);
    }
    public Page<RentalOrder> searchOrders(String customerId, String bikeId, RentalStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return rentalOrderRepository.searchRentalOrders(customerId, bikeId, status, pageable);
    }
    public void disableRentalOrder(int rentalOrderId) {
        RentalOrder order = rentalOrderRepository.findById(rentalOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn thuê có ID: " + rentalOrderId));

        if (order.getRentalOrderActiveStatus() == ActiveStatus.DISABLE) {
            throw new IllegalStateException("Đơn thuê này đã bị vô hiệu hóa trước đó.");
        }

        order.setRentalOrderActiveStatus(ActiveStatus.DISABLE);
        rentalOrderRepository.save(order);
    }
    private void validateRentalOrder(RentalOrder rentalOrder) {
        if (rentalOrder.getRentalOrderCustomerId() == null || rentalOrder.getRentalOrderCustomerId().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng không được để trống");
        }
        if (rentalOrder.getRentalOrderBikeId() == null || rentalOrder.getRentalOrderBikeId().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã xe không được để trống");
        }
        if (rentalOrder.getRentalOrderRentalDate() == null) {
            throw new IllegalArgumentException("Ngày thuê không được để trống");
        }
        if (rentalOrder.getRentalOrderRentalTime() == null) {
            throw new IllegalArgumentException("Giờ thuê không được để trống");
        }

        LocalDate date = rentalOrder.getRentalOrderRentalDate();
        LocalTime time = rentalOrder.getRentalOrderRentalTime();
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày thuê không được lớn hơn ngày hiện tại");
        }
        if (time.isAfter(LocalTime.now()) && date.equals(LocalDate.now())) {
            throw new IllegalArgumentException("Giờ thuê không được lớn hơn thời gian hiện tại");
        }
    }
}