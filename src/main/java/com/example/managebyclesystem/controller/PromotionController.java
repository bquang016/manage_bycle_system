package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.Customer;
import com.example.managebyclesystem.model.Promotion;
import com.example.managebyclesystem.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService){
        this.promotionService = promotionService;
    }

    @GetMapping
    public String getAllPromotion(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Promotion> promotionsPage = promotionService.getAllPromotion(page);
        addPaginationAtttributes(model, promotionsPage, page);
        return "promotions/list";
    }



    private void addPaginationAtttributes(Model model, Page<Promotion> promotionPage, int page){
        model.addAttribute("promotions", promotionPage.getContent());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages", promotionPage.getTotalPages());
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "promotions/add";
    }

    @PostMapping("/add")
    public String addPromotion(@ModelAttribute("promotions") Promotion promotion, Model model) {
        try {
            promotionService.addPromotion(promotion);
            return "redirect:/promotions";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "promotions/add";
        }

    }
}
