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


    //cái này tên là findByStatus có nghĩa là truyền status là able vào thì nó sẽ hiển chị chỉ những thằng able thôi
    @Query("""
        SELECT c FROM Customer c
        WHERE c.status = :status
        """)
    Page<Customer> findByStatus(@Param("status") CustomerStatus status, Pageable pageable);

    @Query("""
        SELECT c FROM Customer c
        WHERE c.status = :status
        ORDER BY c.customerName ASC
        """)
    Page<Customer> findAllByOrderByCustomerNameAsc(@Param("status") CustomerStatus status,Pageable pageable);

    @Query("""
        SELECT c FROM Customer c
        WHERE c.status = :status
        ORDER BY c.customerName DESC
        """)
    Page<Customer> findAllByOrderByCustomerNameDesc(@Param("status") CustomerStatus status,Pageable pageable);


    @Query("""
        SELECT c FROM Customer c
        WHERE c.status = :status
        ORDER BY c.rewardPoints ASC
        """)
    Page<Customer> findAllByOrderByRewardPointsAsc(@Param("status") CustomerStatus status,Pageable pageable);

    @Query("""
        SELECT c FROM Customer c
        WHERE c.status = :status
        ORDER BY c.rewardPoints DESC
        """)
    Page<Customer> findAllByOrderByRewardPointsDesc(@Param("status") CustomerStatus status,Pageable pageable);

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


    //cái này là nó truyền vào keyword thì để cho có thể tìm theo name, email, loại thẻ á
    @Query("""
    SELECT c FROM Customer c
    WHERE
        (:keyword IS NULL OR LOWER(c.customerName) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(c.customerEmail) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(c.cardType) LIKE LOWER(CONCAT('%', :keyword, '%')))
    AND
        (:status IS NULL OR c.status = :status)
    """)
    Page<Customer> searchCustomers(@Param("keyword") String keyword, @Param("status") CustomerStatus status, Pageable pageable);
}
