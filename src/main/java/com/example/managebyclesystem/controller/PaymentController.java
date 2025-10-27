package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.Payment;
import com.example.managebyclesystem.model.Payment.PaymentMethod;
import com.example.managebyclesystem.service.PaymentService;
import com.example.managebyclesystem.service.RentalOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final RentalOrderService rentalOrderService;

    @Autowired
    public PaymentController(PaymentService paymentService, RentalOrderService rentalOrderService) {
        this.paymentService = paymentService;
        this.rentalOrderService = rentalOrderService;
    }


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("payment", new Payment());
        model.addAttribute("rentalOrders", rentalOrderService.getAllActiveRentalOrders());
        model.addAttribute("activeMenu", "payments");
        return "payments/add";
    }

    @PostMapping("/add")
    public String addPayment(@ModelAttribute Payment payment, Model model) {
        try {
            paymentService.addPayment(payment);
            model.addAttribute("message", "Thêm thanh toán thành công!");
            return "redirect:/payments";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("rentalOrders", rentalOrderService.getAllActiveRentalOrders());
            model.addAttribute("activeMenu", "payments");
            return "payments/add";
        }
    }



    @PostMapping("/delete/{id}")
    public String deletePayment(@PathVariable("id") int id, Model model) {
        try {
            paymentService.deletePayment(id);
            model.addAttribute("message", "Vô hiệu hóa thanh toán thành công!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/payments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Payment payment = paymentService.getPaymentById(id);
        model.addAttribute("payment", payment);
        model.addAttribute("rentalOrders", rentalOrderService.getAllActiveRentalOrders());
        model.addAttribute("activeMenu", "payments");
        return "payments/edit";
    }


    @PostMapping("/edit")
    public String updatePayment(@ModelAttribute("payment") Payment updatedPayment, Model model) {
        try {
            paymentService.updatePayment(updatedPayment);
            return "redirect:/payments";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("rentalOrders", rentalOrderService.getAllActiveRentalOrders());
            model.addAttribute("activeMenu", "payments");
            return "payments/edit";
        }
    }


    @GetMapping("/search")
    public String searchPayments(
            @RequestParam(required = false) Integer rentalOrderId,
            @RequestParam(required = false) PaymentMethod paymentMethod,
            Model model
    ) {
        try {
            var results = paymentService.searchPayments(rentalOrderId, paymentMethod);
            model.addAttribute("payments", results);
            model.addAttribute("message", "Tìm thấy " + results.size() + " kết quả.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("payments", paymentService.getAllActivePayments());
        }

        model.addAttribute("rentalOrders", rentalOrderService.getAllActiveRentalOrders());
        model.addAttribute("rentalOrderId", rentalOrderId);
        model.addAttribute("selectedMethod", paymentMethod);
        model.addAttribute("activeMenu", "payments");

        return "payments/list";
    }


    // phân trang + sort
    @GetMapping
    public String listPayments(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "paymentDate") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model
    ) {
        Page<Payment> paymentPage = paymentService.getPaginatedAndSortedPayments(pageNo, pageSize, sortField, sortDir);

        model.addAttribute("payments", paymentPage.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", paymentPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("isSearchResult", false);
        model.addAttribute("activeMenu", "payments");
        model.addAttribute("rentalOrders", rentalOrderService.getAllActiveRentalOrders());
        return "payments/list";
    }
}
