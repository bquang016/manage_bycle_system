package com.example.managebyclesystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Check(constraints = "payment_status IN ('Able','Disable') AND payment_method IN ('CASH','TRANSFER')")
public class Payment {

    public enum PaymentMethod {
        CASH,
        TRANSFER
    }

    public enum PaymentStatus {
        Able,
        Disable
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int paymentId;


    @ManyToOne
    @JoinColumn(name = "rental_order_id", nullable = false)
    private RentalOrder rentalOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_amount", nullable = false)
    private double paymentAmount;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate = LocalDate.now(); // mac dinh htai

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.Able;

    public Payment() {
    }

    public Payment(RentalOrder rentalOrder, PaymentMethod paymentMethod, double paymentAmount, LocalDate paymentDate) {
        this.rentalOrder = rentalOrder;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.paymentDate = LocalDate.now();
        this.paymentStatus = PaymentStatus.Able;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public RentalOrder getRentalOrder() {
        return rentalOrder;
    }

    public void setRentalOrder(RentalOrder rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
