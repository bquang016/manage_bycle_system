package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.RentalOrder;
import com.example.managebyclesystem.model.RentalOrder.RentalStatus;
import com.example.managebyclesystem.service.RentalOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rental-orders")
public class RentalOrderController {

    private final RentalOrderService rentalOrderService;
    private static final int PAGE_SIZE = 10;

    @Autowired
    public RentalOrderController(RentalOrderService rentalOrderService) {
        this.rentalOrderService = rentalOrderService;
    }

    // ---------------- HELPER ----------------
    private void addPaginationAttributes(Model model, Page<RentalOrder> orderPage, int page) {
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
    }

    // ---------------- LIST ALL ----------------
    @GetMapping
    public String getAllOrders(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<RentalOrder> orderPage = rentalOrderService.getAllOrders(page, PAGE_SIZE);
        addPaginationAttributes(model, orderPage, page);
        return "rental_orders/list";
    }

    // ---------------- SORTING ----------------
    @GetMapping("/dateAsc")
    public String getDateAsc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<RentalOrder> orderPage = rentalOrderService.getAllByOrderByRentalDateAsc(page, PAGE_SIZE);
        addPaginationAttributes(model, orderPage, page);
        return "rental_orders/list";
    }

    @GetMapping("/dateDesc")
    public String getDateDesc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<RentalOrder> orderPage = rentalOrderService.getAllByOrderByRentalDateDesc(page, PAGE_SIZE);
        addPaginationAttributes(model, orderPage, page);
        return "rental_orders/list";
    }

    @GetMapping("/totalAsc")
    public String getTotalAsc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<RentalOrder> orderPage = rentalOrderService.getAllByOrderByTotalAmountAsc(page, PAGE_SIZE);
        addPaginationAttributes(model, orderPage, page);
        return "rental_orders/list";
    }

    @GetMapping("/totalDesc")
    public String getTotalDesc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<RentalOrder> orderPage = rentalOrderService.getAllByOrderByTotalAmountDesc(page, PAGE_SIZE);
        addPaginationAttributes(model, orderPage, page);
        return "rental_orders/list";
    }
}