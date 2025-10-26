package com.example.managebyclesystem.repository;

import com.example.managebyclesystem.model.Payment;
import com.example.managebyclesystem.model.Payment.PaymentStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByPaymentStatus(PaymentStatus status); // list able


    @Query("""
        SELECT p FROM Payment p
        WHERE p.paymentStatus = 'Able'
        AND (
            (:rentalOrderId IS NULL OR p.rentalOrder.rentalOrderId = :rentalOrderId)
            OR (:paymentMethod IS NULL OR p.paymentMethod = :paymentMethod)
        )
    """)
    List<Payment> searchPayments(@Param("rentalOrderId") Integer rentalOrderId,
                                 @Param("paymentMethod") Payment.PaymentMethod paymentMethod);

    // phân trang + sort able
    Page<Payment> findByPaymentStatus(PaymentStatus status, Pageable pageable);
}
