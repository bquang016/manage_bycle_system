package com.example.managebyclesystem.repository;


import com.example.managebyclesystem.constants.PromotionStatus;
import com.example.managebyclesystem.model.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepo extends JpaRepository<Promotion, Integer> {

    @Query("""
        select p from Promotion p
        where p.promotionStatus = :status
        """)
    Page<Promotion> findByStatus (@Param("status") PromotionStatus promotion, Pageable pageable);


    @Query("""
        select p from Promotion p
        where p.promotionStatus = :status
        order by p.promotionName asc
        """)
    Page<Promotion> findAllByOrderByPromotionNameAsc(@Param("status") PromotionStatus promotion,Pageable pageable);

    @Query("""
        select p from Promotion p
        where p.promotionStatus = :status
        order by p.promotionName desc
        """)
    Page<Promotion> findAllByOrderByPromotionNameDesc(@Param("status") PromotionStatus promotion,Pageable pageable);

    @Query("""
        select p from Promotion p
        where p.promotionStatus = :status
        order by p.promotionDiscount asc
        """)
    Page<Promotion> findAllByOrderByPromotionDiscountAsc(@Param("status") PromotionStatus promotion,Pageable pageable);

    @Query("""
        select p from Promotion p
        where p.promotionStatus = :status
        order by p.promotionDiscount desc
        """)
    Page<Promotion> findAllByOrderPromotionDiscountDesc(@Param("status") PromotionStatus promotion,Pageable pageable);
}
