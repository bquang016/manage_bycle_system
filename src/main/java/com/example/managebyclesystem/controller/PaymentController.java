package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.Payment;
import com.example.managebyclesystem.model.Payment.PaymentMethod;
import com.example.managebyclesystem.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("payment", new Payment());
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
            return "payments/add";
        }
    }

    // lst ale
    @GetMapping
    public String listPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllActivePayments());
        return "payments/list";
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
        return "payments/edit";
    }


    @PostMapping("/edit")
    public String updatePayment(@ModelAttribute("payment") Payment updatedPayment, Model model) {
        try {
            paymentService.updatePayment(updatedPayment);
            return "redirect:/payments";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
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

        model.addAttribute("rentalOrderId", rentalOrderId);
        model.addAttribute("selectedMethod", paymentMethod);

        return "payments/list";
    }
}
