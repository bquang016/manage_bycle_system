package com.example.managebyclesystem.controller;


import com.example.managebyclesystem.model.Customer;
import com.example.managebyclesystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customers/add";
    }

    @PostMapping("/add")
    public String addCustomer(@ModelAttribute("customer") Customer customer, Model model) {
        try {
            customerService.addCustomer(customer);
            return "redirect:/customers";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "customers/add";
        }

    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Customer customer = customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng id: " + id));
        model.addAttribute("customer", customer);
        return "customers/edit";
    }


    @PostMapping("/edit/{id}")
    public String updateCustomer(@PathVariable int id, @ModelAttribute("customer") Customer newCustomer, Model model) {
        try {
            customerService.updateCustomer(id, newCustomer);
            return "redirect:/customers";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "customers/edit";
        }
    }

    @GetMapping("/search/name")
    public String searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Page<Customer> customerPage = customerService.getCustomerByName(name, page);
        return prepareSearchModel(model, customerPage, page, "name", name);
    }

    @GetMapping("/search/email")
    public String searchByEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Page<Customer> customerPage = customerService.getCustomerByEmail(email, page);
        return prepareSearchModel(model, customerPage, page, "email", email);
    }

    @GetMapping("/search/cardType")
    public String searchByCardType(
            @RequestParam String cardType,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Page<Customer> customerPage = customerService.getCustomerByCardType(cardType, page);
        return prepareSearchModel(model, customerPage, page, "cardType", cardType);
    }
    private String prepareSearchModel(
            Model model,
            Page<Customer> customerPage,
            int page,
            String searchType,
            String keyword
    ) {
        model.addAttribute("customers", customerPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customerPage.getTotalPages());
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        return "customers/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }

}
