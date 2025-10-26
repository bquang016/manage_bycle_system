package com.example.managebyclesystem.model;

import com.example.managebyclesystem.constants.PromotionStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int promoId;

    private String promoName;
    private String promoType;
    private double promoDiscount;

    private LocalDate promoStartDate;
    private LocalDate promoEndDate;

    @Enumerated(EnumType.STRING)
    private PromotionStatus promoStatus = PromotionStatus.ABLE;

    public Promotion() {
    }

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public String getPromoType() {
        return promoType;
    }

    public void setPromoType(String promoType) {
        this.promoType = promoType;
    }

    public double getPromoDiscount() {
        return promoDiscount;
    }

    public void setPromoDiscount(double promoDiscount) {
        this.promoDiscount = promoDiscount;
    }

    public LocalDate getPromoStartDate() {
        return promoStartDate;
    }

    public void setPromoStartDate(LocalDate promoStartDate) {
        this.promoStartDate = promoStartDate;
    }

    public LocalDate getPromoEndDate() {
        return promoEndDate;
    }

    public void setPromoEndDate(LocalDate promoEndDate) {
        this.promoEndDate = promoEndDate;
    }

    public PromotionStatus getPromoStatus() {
        return promoStatus;
    }

    public void setPromoStatus(PromotionStatus promoStatus) {
        this.promoStatus = promoStatus;
    }
}