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


    @Query("""
        SELECT p FROM Promotion p
        WHERE
            (:keyword IS NULL OR LOWER(p.promotionName) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(p.promotionType) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND
            (:status IS NULL OR p.promotionStatus = :status)
        """)
    Page<Promotion> searchPromotions(@Param("keyword") String keyword, @Param("status") PromotionStatus status, Pageable pageable);
}
