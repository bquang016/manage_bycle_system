package com.example.managebyclesystem.repository;

import com.example.managebyclesystem.model.RentalOrder;
import com.example.managebyclesystem.model.RentalOrder.RentalStatus;
import com.example.managebyclesystem.model.RentalOrder.ActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalOrderRepository extends JpaRepository<RentalOrder, Integer> {

    @Query("""
        SELECT r FROM RentalOrder r
        WHERE r.rentalOrderActiveStatus = :status
    """)
    Page<RentalOrder> findByActiveStatus(@Param("status") ActiveStatus status, Pageable pageable);

    @Query("SELECT r FROM RentalOrder r ORDER BY r.rentalOrderRentalDate ASC")
    Page<RentalOrder> findAllByOrderByRentalDateAsc(Pageable pageable);

    @Query("SELECT r FROM RentalOrder r ORDER BY r.rentalOrderRentalDate DESC")
    Page<RentalOrder> findAllByOrderByRentalDateDesc(Pageable pageable);

    @Query("SELECT r FROM RentalOrder r ORDER BY r.rentalOrderTotalAmount ASC")
    Page<RentalOrder> findAllByOrderByTotalAmountAsc(Pageable pageable);

    @Query("SELECT r FROM RentalOrder r ORDER BY r.rentalOrderTotalAmount DESC")
    Page<RentalOrder> findAllByOrderByTotalAmountDesc(Pageable pageable);

    @Query("""
        SELECT r FROM RentalOrder r
        WHERE (:customerId IS NULL OR LOWER(r.rentalOrderCustomerId) LIKE LOWER(CONCAT('%', :customerId, '%')))
          AND (:bikeId IS NULL OR LOWER(r.rentalOrderBikeId) LIKE LOWER(CONCAT('%', :bikeId, '%')))
          AND (:status IS NULL OR r.rentalOrderStatus = :status)
          AND r.rentalOrderActiveStatus = com.example.managebyclesystem.model.RentalOrder.ActiveStatus.ABLE
    """)
    Page<RentalOrder> searchRentalOrders(
            @Param("customerId") String customerId,
            @Param("bikeId") String bikeId,
            @Param("status") RentalStatus status,
            Pageable pageable
    );
}
