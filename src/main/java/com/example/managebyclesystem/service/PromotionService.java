package com.example.managebyclesystem.service;


import com.example.managebyclesystem.constants.PromotionStatus;
import com.example.managebyclesystem.model.Promotion;
import com.example.managebyclesystem.repository.PromotionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PromotionService {

    private final PromotionRepo promotionRepo;
    private static final int PAGE_SIZE =10;


    @Autowired
    public PromotionService(PromotionRepo promotionRepo){
        this.promotionRepo = promotionRepo;
    }

    public Page<Promotion> getAllPromotion(int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return promotionRepo.findByStatus(PromotionStatus.ABLE, pageable);
    }

    public Page<Promotion> getAllOrderByNameAsc(int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return promotionRepo.findAllByOrderByPromotionNameAsc(PromotionStatus.ABLE,pageable);
    }

    public Page<Promotion> getAllOrderByNameDesc(int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return promotionRepo.findAllByOrderByPromotionNameDesc(PromotionStatus.ABLE,pageable);
    }

    public Page<Promotion> getAllOrderByDiscountAsc(int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return promotionRepo.findAllByOrderByPromotionDiscountAsc(PromotionStatus.ABLE,pageable);
    }

    public Page<Promotion> getAllOrderByDiscountDesc(int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return  promotionRepo.findAllByOrderPromotionDiscountDesc(PromotionStatus.ABLE,pageable);
    }

    public Promotion addPromotion(Promotion promotion) {
        if (promotion.getPromotionName() == null || promotion.getPromotionName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khuyến mãi không được để trống");
        }

        if (promotion.getPromotionType() == null) {
            throw new IllegalArgumentException("Loại khuyến mãi không được để trống");
        }

        if (promotion.getPromotionStartDate() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu khuyến mãi không được để trống");
        }

        if (promotion.getPromotionEndDate() == null) {
            throw new IllegalArgumentException("Ngày kết thúc khuyến mãi không được để trống");
        }

        if (promotion.getPromotionDiscount() < 0) {
            throw new IllegalArgumentException("Giảm giá phải lớn hơn hoặc bằng 0%");
        }
        if (promotion.getPromotionDiscount() > 100) {
            throw new IllegalArgumentException("Giảm giá không được vượt quá 100%");
        }

        LocalDate now = LocalDate.now();
        if (promotion.getPromotionEndDate().isBefore(promotion.getPromotionStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc không được nhỏ hơn ngày bắt đầu");
        }
        if (promotion.getPromotionEndDate().isBefore(now)) {
            throw new IllegalArgumentException("Ngày kết thúc không được nằm trong quá khứ");
        }

        return promotionRepo.save(promotion);
    }
}
