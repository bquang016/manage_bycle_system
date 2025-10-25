package com.example.managebyclesystem.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "rental_orders")
public class RentalOrder {

    public enum RentalStatus {
        PENDING,
        ONGOING,
        COMPLETED,
        CANCELLED
    }

    public enum ActiveStatus {
        ABLE,
        DISABLE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_order_id")
    private int rentalOrderId;

    @Column(name = "rental_order_customer_id", nullable = false)
    private String rentalOrderCustomerId;

    @Column(name = "rental_order_bike_id", nullable = false)
    private String rentalOrderBikeId;

    @Column(name = "rental_order_rental_date", nullable = false)
    private LocalDate rentalOrderRentalDate;

    @Column(name = "rental_order_rental_time", nullable = false)
    private LocalTime rentalOrderRentalTime;

    @Column(name = "rental_order_total_amount")
    private double rentalOrderTotalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "rental_order_status")
    private RentalStatus rentalOrderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "rental_order_active_status")
    private ActiveStatus rentalOrderActiveStatus;

    public int getRentalOrderId() {
        return rentalOrderId;
    }

    public void setRentalOrderId(int rentalOrderId) {
        this.rentalOrderId = rentalOrderId;
    }

    public String getRentalOrderCustomerId() {
        return rentalOrderCustomerId;
    }

    public void setRentalOrderCustomerId(String rentalOrderCustomerId) {
        this.rentalOrderCustomerId = rentalOrderCustomerId;
    }

    public String getRentalOrderBikeId() {
        return rentalOrderBikeId;
    }

    public void setRentalOrderBikeId(String rentalOrderBikeId) {
        this.rentalOrderBikeId = rentalOrderBikeId;
    }

    public LocalDate getRentalOrderRentalDate() {
        return rentalOrderRentalDate;
    }

    public void setRentalOrderRentalDate(LocalDate rentalOrderRentalDate) {
        this.rentalOrderRentalDate = rentalOrderRentalDate;
    }

    public LocalTime getRentalOrderRentalTime() {
        return rentalOrderRentalTime;
    }

    public void setRentalOrderRentalTime(LocalTime rentalOrderRentalTime) {
        this.rentalOrderRentalTime = rentalOrderRentalTime;
    }

    public double getRentalOrderTotalAmount() {
        return rentalOrderTotalAmount;
    }

    public void setRentalOrderTotalAmount(double rentalOrderTotalAmount) {
        this.rentalOrderTotalAmount = rentalOrderTotalAmount;
    }

    public RentalStatus getRentalOrderStatus() {
        return rentalOrderStatus;
    }

    public void setRentalOrderStatus(RentalStatus rentalOrderStatus) {
        this.rentalOrderStatus = rentalOrderStatus;
    }

    public ActiveStatus getRentalOrderActiveStatus() {
        return rentalOrderActiveStatus;
    }

    public void setRentalOrderActiveStatus(ActiveStatus rentalOrderActiveStatus) {
        this.rentalOrderActiveStatus = rentalOrderActiveStatus;
    }
}
