package com.example.managebyclesystem.service;


import com.example.managebyclesystem.constants.PromotionStatus;
import com.example.managebyclesystem.model.Promotion;
import com.example.managebyclesystem.repository.PromotionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}
