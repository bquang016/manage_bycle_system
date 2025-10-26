package com.example.managebyclesystem.controller;

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

    @GetMapping("/promotions")
    public String getAllPromotions(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Promotion> promotionsPage = promotionService.getAllPromotion(page);
        addPaginationAttributes(model, promotionsPage, page);
        return "promotions/list";
    }


    @GetMapping("/sort/name")
    public String getPromotionsSortedByName(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "asc") String order, Model model) {
        Page<Promotion> promotionPage;

        if ("desc".equalsIgnoreCase(order)) {
            promotionPage = promotionService.getAllOrderByNameDesc(page);
        } else {
            promotionPage = promotionService.getAllOrderByNameAsc(page);
        }

        addPaginationAttributes(model, promotionPage, page);
        model.addAttribute("sortType", "name");
        model.addAttribute("order", order);
        return "promotions/list";
    }


    @GetMapping("/sort/discount")
    public String getPromotionsSortedByDiscount(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "asc") String order, Model model) {
        Page<Promotion> promotionPage;

        if ("desc".equalsIgnoreCase(order)) {
            promotionPage = promotionService.getAllOrderByDiscountDesc(page);
        } else {
            promotionPage = promotionService.getAllOrderByDiscountAsc(page);
        }

        addPaginationAttributes(model, promotionPage, page);
        model.addAttribute("sortType", "discount");
        model.addAttribute("order", order);
        return "promotions/list";
    }

    private void addPaginationAttributes(Model model, Page<Promotion> promotionPage, int page) {
        model.addAttribute("promotions", promotionPage.getContent());
        model.addAttribute("currentPage", page);
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

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Promotion promotion = promotionService.getPromotionById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng id: " + id));
        model.addAttribute("promotion", promotion);
        return "promotions/edit";
    }


    @PostMapping("/edit/{id}")
    public String updatePromotion(@PathVariable int id, @ModelAttribute("promotion") Promotion newPromotion, Model model) {
        try {
            promotionService.updatePromotion(id, newPromotion);
            return "redirect:/promotions";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "promotions/edit";
        }
    }

    @GetMapping("delete/{id}")
    public String deletePromotion(@PathVariable int id){
        promotionService.deletePromotion(id);
        return "redirect:/promotions";
    }

    @GetMapping("/search/name")
    public String searchByName(@RequestParam String name, @RequestParam(defaultValue = "0") int page, Model model){
        Page<Promotion> promotionPage = promotionService.getPromotionByName(name, page);
        return prepareSearchModel(model, promotionPage, page,"name", name);
    }

    @GetMapping("/search/type")
    public String searchByType(@RequestParam String type, @RequestParam(defaultValue = "0") int page, Model model){
        Page<Promotion> promotionPage = promotionService.getPromotionByType(type, page);
        return prepareSearchModel(model, promotionPage, page,"type", type);
    }

    private String prepareSearchModel(Model model, Page<Promotion> promotionPage, int page, String searchType, String keyword) {
        model.addAttribute("promotions", promotionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", promotionPage.getTotalPages());
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        return "promotions/list";
    }

    @GetMapping("/apply")
    public String showApplyPromotionForm() {
        return "promotions/apply";
    }

    @PostMapping("/apply")
    public String applyPromotion(@RequestParam("promotionId") int promotionId, @RequestParam("price") double price, Model model) {
        try {
            double finalPrice = promotionService.applyPromotion(promotionId, price);

            model.addAttribute("promotionId", promotionId);
            model.addAttribute("originalPrice", price);
            model.addAttribute("finalPrice", finalPrice);
            model.addAttribute("message", "Áp dụng khuyến mãi thành công!");

            return "promotions/result";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "promotions/error";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "promotions/error";
        }

    }

}
