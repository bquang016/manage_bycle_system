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

    private void addPaginationAttributes(Model model, Page<RentalOrder> orderPage, int page) {
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
    }

    @GetMapping
    public String getAllOrders(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<RentalOrder> orderPage = rentalOrderService.getAllOrders(page, PAGE_SIZE);
        addPaginationAttributes(model, orderPage, page);
        return "rental_orders/list";
    }

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
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("rentalOrder", new RentalOrder());
        return "rental_orders/add";
    }

    @PostMapping("/add")
    public String addOrder(@ModelAttribute("rentalOrder") RentalOrder rentalOrder, Model model) {
        try {
            rentalOrderService.addRentalOrder(rentalOrder);
            return "redirect:/rental-orders";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "rental_orders/add";
        }
    }
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable int id, Model model) {
        RentalOrder rentalOrder = rentalOrderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê ID: " + id));
        model.addAttribute("rentalOrder", rentalOrder);
        return "rental_orders/edit";
    }

    @PostMapping("/edit")
    public String updateOrder(@ModelAttribute("rentalOrder") RentalOrder rentalOrder, Model model) {
        try {
            rentalOrderService.updateRentalOrder(rentalOrder);
            return "redirect:/rental-orders";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "rental_orders/edit";
        }
    }
    @GetMapping("/search")
    public String searchOrders(@RequestParam(required = false) String customerId,
                               @RequestParam(required = false) String bikeId,
                               @RequestParam(required = false) RentalStatus status,
                               @RequestParam(defaultValue = "0") int page,
                               Model model) {
        Page<RentalOrder> orderPage = rentalOrderService.searchOrders(customerId, bikeId, status, page, PAGE_SIZE);
        addPaginationAttributes(model, orderPage, page);
        model.addAttribute("searchCustomerId", customerId);
        model.addAttribute("searchBikeId", bikeId);
        model.addAttribute("searchStatus", status);
        return "rental_orders/list";
    }
    @PostMapping("/delete/{id}")
    public String disableOrder(@PathVariable int id) {
        rentalOrderService.disableRentalOrder(id);
        return "redirect:/rental-orders";
    }
}
