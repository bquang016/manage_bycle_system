package com.example.managebyclesystem.model;

import com.example.managebyclesystem.constants.PromotionStatus;
import com.example.managebyclesystem.constants.PromotionType;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int promotionId;

    private String promotionName;

    @Enumerated(EnumType.STRING)
    private PromotionType promotionType;

    private double promotionDiscount;

    private LocalDate promotionStartDate;
    private LocalDate promotionEndDate;

    @Enumerated(EnumType.STRING)
    private PromotionStatus promotionStatus = PromotionStatus.ABLE;

    public Promotion() {

    }

    public Promotion(String promotionName, PromotionType promotionType, double promotionDiscount,
                     LocalDate promotionStartDate, LocalDate promotionEndDate, PromotionStatus promotionStatus) {
        this.promotionName = promotionName;
        this.promotionType = promotionType;
        this.promotionDiscount = promotionDiscount;
        this.promotionStartDate = promotionStartDate;
        this.promotionEndDate = promotionEndDate;
        this.promotionStatus = promotionStatus;
    }

    // Getters & Setters
    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promoId) {
        this.promotionId = promoId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(PromotionType promotionType) {
        this.promotionType = promotionType;
    }

    public double getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(double promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public LocalDate getPromotionStartDate() {
        return promotionStartDate;
    }

    public void setPromotionStartDate(LocalDate promotionStartDate) {
        this.promotionStartDate = promotionStartDate;
    }

    public LocalDate getPromotionEndDate() {
        return promotionEndDate;
    }

    public void setPromotionEndDate(LocalDate promotionEndDate) {
        this.promotionEndDate = promotionEndDate;
    }

    public PromotionStatus getPromotionStatus() {
        return promotionStatus;
    }

    public void setPromotionStatus(PromotionStatus promotionStatus) {
        this.promotionStatus = promotionStatus;
    }
}
