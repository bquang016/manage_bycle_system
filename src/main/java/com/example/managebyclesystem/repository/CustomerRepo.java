package com.example.managebyclesystem.repository;

import com.example.managebyclesystem.constants.CustomerStatus;
import com.example.managebyclesystem.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    @Query("""
        SELECT c FROM Customer c
        WHERE c.status = :status
        """)
    Page<Customer> findByStatus(@Param("status") CustomerStatus status, Pageable pageable);

    @Query("""
        SELECT c FROM Customer c
        ORDER BY c.customerName ASC
        """)
    Page<Customer> findAllByOrderByCustomerNameAsc(Pageable pageable);

    @Query("""
        SELECT c FROM Customer c
        ORDER BY c.customerName DESC
        """)
    Page<Customer> findAllByOrderByCustomerNameDesc(Pageable pageable);


    @Query("""
        SELECT c FROM Customer c
        ORDER BY c.rewardPoints ASC
        """)
    Page<Customer> findAllByOrderByRewardPointsAsc(Pageable pageable);

    @Query("""
        SELECT c FROM Customer c
        ORDER BY c.rewardPoints DESC
        """)
    Page<Customer> findAllByOrderByRewardPointsDesc(Pageable pageable);

    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END
        FROM Customer c
        WHERE c.customerPhone = :customerPhone
        """)
    boolean existsByCustomerPhone(@Param("customerPhone") String customerPhone);

    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END
        FROM Customer c
        WHERE c.customerEmail = :customerEmail
        """)
    boolean existsByCustomerEmail(@Param("customerEmail") String customerEmail);
}
