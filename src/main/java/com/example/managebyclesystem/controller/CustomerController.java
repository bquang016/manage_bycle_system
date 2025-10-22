package com.example.managebyclesystem.controller;


import com.example.managebyclesystem.model.Customer;
import com.example.managebyclesystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String getAllCustomers(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Customer> customerPage = customerService.getAllCustomer(page);
        addPaginationAttributes(model, customerPage, page);
        return "customers/list";
    }
    @GetMapping("/nameAsc")
    public String getCustomersNameAsc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Customer> customerPage = customerService.getAllByOrderByNameAsc(page);
        addPaginationAttributes(model, customerPage, page);
        return "customers/list";
    }

    @GetMapping("/nameDesc")
    public String getCustomersNameDesc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Customer> customerPage = customerService.getAllByOrderByNameDesc(page);
        addPaginationAttributes(model, customerPage, page);
        return "customers/list";
    }

    @GetMapping("/pointAsc")
    public String getCustomersPointAsc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Customer> customerPage = customerService.getAllByOrderByRewardPointAsc(page);
        addPaginationAttributes(model, customerPage, page);
        return "customers/list";
    }

    @GetMapping("/pointDesc")
    public String getCustomersPointDesc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Customer> customerPage = customerService.getAllByOrderByRewardPointDesc(page);
        addPaginationAttributes(model, customerPage, page);
        return "customers/list";
    }

    private void addPaginationAttributes(Model model, Page<Customer> customerPage, int page) {
        //lấy danh sách khách hanng ở trang hiện tại
        model.addAttribute("customers", customerPage.getContent());
        //đưa cái danh sách đó sang view
        model.addAttribute("currentPage", page);
        //lấy tổng số trang của cái phân trang á
        model.addAttribute("totalPages", customerPage.getTotalPages());
    }
}
