package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.Promotion;
import com.example.managebyclesystem.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService){
        this.promotionService = promotionService;
    }

    @GetMapping
    public String getAllPromotions(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Promotion> promotionsPage = promotionService.getAllPromotion(page);
        addPaginationAtttributes(model, promotionsPage, page);
        return "promotions/list";
    }

    @GetMapping
    public String getPromotionsNameAsc(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Promotion> promotionPage = promotionService.getAllOrderByNameAsc(page);
        addPaginationAtttributes(model, promotionPage, page);
        return "promotions/list";
    }
    @GetMapping
    public String getPromotionsNameDesc(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Promotion> promotionPage = promotionService.getAllOrderByNameDesc(page);
        addPaginationAtttributes(model, promotionPage, page);
        return "promotions/list";
    }

    @GetMapping
    public String getPromotionsDiscountAsc(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Promotion> promotionPage = promotionService.getAllOrderByDiscountAsc(page);
        addPaginationAtttributes(model, promotionPage, page);
        return "promotions/list";
    }
    @GetMapping
    public String getPromotionsDiscountDesc(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Promotion> promotionPage = promotionService.getAllOrderByDiscountDesc(page);
        addPaginationAtttributes(model, promotionPage, page);
        return "promotions/list";
    }



    private void addPaginationAtttributes(Model model, Page<Promotion> promotionPage, int page){
        model.addAttribute("promotions", promotionPage.getContent());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages", promotionPage.getTotalPages());
    }
}
