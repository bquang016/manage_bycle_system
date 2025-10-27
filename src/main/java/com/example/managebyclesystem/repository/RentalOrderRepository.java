package com.example.managebyclesystem.repository;

import com.example.managebyclesystem.model.RentalOrder;
import com.example.managebyclesystem.model.RentalOrder.RentalStatus;
import com.example.managebyclesystem.model.RentalOrder.ActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RentalOrderRepository extends JpaRepository<RentalOrder, Integer> {

    // Find pageable active orders by enum value
    @Query("SELECT r FROM RentalOrder r WHERE r.rentalOrderActiveStatus = :status")
    Page<RentalOrder> findByRentalOrderActiveStatus(@Param("status") ActiveStatus status, Pageable pageable);

    @Query("SELECT r FROM RentalOrder r ORDER BY r.rentalOrderRentalDate ASC")
    Page<RentalOrder> findAllByOrderByRentalDateAsc(Pageable pageable);

    @Query("SELECT r FROM RentalOrder r ORDER BY r.rentalOrderRentalDate DESC")
    Page<RentalOrder> findAllByOrderByRentalDateDesc(Pageable pageable);

    @Query("SELECT r FROM RentalOrder r ORDER BY r.rentalOrderTotalAmount ASC")
    Page<RentalOrder> findAllByOrderByTotalAmountAsc(Pageable pageable);

    @Query("SELECT r FROM RentalOrder r ORDER BY r.rentalOrderTotalAmount DESC")
    Page<RentalOrder> findAllByOrderByTotalAmountDesc(Pageable pageable);

    // List all active rental orders (non-pageable)
    List<RentalOrder> findByRentalOrderActiveStatus(ActiveStatus status);

    // Search by customer name, bike name and status (optional params). Uses nested properties.
    @Query("""
        SELECT r FROM RentalOrder r
        WHERE (:customerName IS NULL OR LOWER(r.customerId.customerName) LIKE LOWER(CONCAT('%', :customerName, '%')))
          AND (:bikeName IS NULL OR LOWER(r.bikeId.bikeName) LIKE LOWER(CONCAT('%', :bikeName, '%')))
          AND (:status IS NULL OR r.rentalOrderStatus = :status)
    """)
    Page<RentalOrder> searchRentalOrders(
            @Param("customerName") String customerName,
            @Param("bikeName") String bikeName,
            @Param("status") RentalStatus status,
            Pageable pageable
    );
}
