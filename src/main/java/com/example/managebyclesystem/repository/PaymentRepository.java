package com.example.managebyclesystem.repository;

import com.example.managebyclesystem.model.Payment;
import com.example.managebyclesystem.model.Payment.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByPaymentStatus(PaymentStatus status); // list able

    Page<Payment> findByPaymentStatusNot(PaymentStatus status, Pageable pageable); //phan trang able
}
