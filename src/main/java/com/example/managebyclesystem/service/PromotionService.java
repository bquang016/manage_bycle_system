package com.example.managebyclesystem.service;


import com.example.managebyclesystem.constants.PromotionStatus;
import com.example.managebyclesystem.model.Customer;
import com.example.managebyclesystem.model.Promotion;
import com.example.managebyclesystem.repository.PromotionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

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

        LocalDate today = LocalDate.now();
        promotion.setPromotionStartDate(today);

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

    public Optional<Promotion> getPromotionById(int id) {
        return promotionRepo.findById(id);
    }


    public Promotion updatePromotion(int id, Promotion newPromotionData) {
        Promotion existing = promotionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi có id: " + id));

        boolean changed = false;

        if (newPromotionData.getPromotionName() != null
                && !Objects.equals(existing.getPromotionName(), newPromotionData.getPromotionName())) {
            existing.setPromotionName(newPromotionData.getPromotionName());
            changed = true;
        }

        if (newPromotionData.getPromotionType() != null
                && !Objects.equals(existing.getPromotionType(), newPromotionData.getPromotionType())) {
            existing.setPromotionType(newPromotionData.getPromotionType());
            changed = true;
        }

        if (newPromotionData.getPromotionDiscount() != existing.getPromotionDiscount()) {
            if (newPromotionData.getPromotionDiscount() < 0 || newPromotionData.getPromotionDiscount() > 100) {
                throw new IllegalArgumentException("Giảm giá phải nằm trong khoảng 0% đến 100%");
            }
            existing.setPromotionDiscount(newPromotionData.getPromotionDiscount());
            changed = true;
        }

        if (newPromotionData.getPromotionStartDate() != null
                && !Objects.equals(existing.getPromotionStartDate(), newPromotionData.getPromotionStartDate())) {
            if (newPromotionData.getPromotionEndDate() != null &&
                    newPromotionData.getPromotionEndDate().isBefore(newPromotionData.getPromotionStartDate())) {
                throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc");
            }
            existing.setPromotionStartDate(newPromotionData.getPromotionStartDate());
            changed = true;
        }

        if (newPromotionData.getPromotionEndDate() != null
                && !Objects.equals(existing.getPromotionEndDate(), newPromotionData.getPromotionEndDate())) {
            if (newPromotionData.getPromotionStartDate() != null &&
                    newPromotionData.getPromotionEndDate().isBefore(newPromotionData.getPromotionStartDate())) {
                throw new IllegalArgumentException("Ngày kết thúc không được trước ngày bắt đầu");
            }
            if (newPromotionData.getPromotionEndDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Ngày kết thúc không được nằm trong quá khứ");
            }
            existing.setPromotionEndDate(newPromotionData.getPromotionEndDate());
            changed = true;
        }

        return changed ? promotionRepo.save(existing) : existing;
    }

    public void deletePromotion(int id){
        Promotion existing = promotionRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy promotion có id: "+id));
        existing.setPromotionStatus(PromotionStatus.DISABLE);
        promotionRepo.save(existing);
    }

    public Page<Promotion> getPromotionByName(String name, int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return promotionRepo.searchPromotions(name, PromotionStatus.ABLE, pageable);
    }

    public Page<Promotion> getPromotionByType(String type, int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return promotionRepo.searchPromotions(type, PromotionStatus.ABLE, pageable);
    }

}
