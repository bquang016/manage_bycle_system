package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.constants.CustomerStatus;
import com.example.managebyclesystem.constants.PromotionStatus;
import com.example.managebyclesystem.model.RentalOrder;
import com.example.managebyclesystem.model.RentalOrder.RentalStatus;
import com.example.managebyclesystem.service.RentalOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.managebyclesystem.model.*;
import com.example.managebyclesystem.service.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rental-orders")
public class RentalOrderController {

    private final RentalOrderService rentalOrderService;
    private final CustomerService customerService;
    private final BikeService bikeService;
    private final PromotionService promotionService;
    private static final int PAGE_SIZE = 10;

    @Autowired
    public RentalOrderController(RentalOrderService rentalOrderService, CustomerService customerService,
                                 BikeService bikeService, PromotionService promotionService) {
        this.rentalOrderService = rentalOrderService;
        this.customerService = customerService;
        this.bikeService = bikeService;
        this.promotionService = promotionService;
    }

    private void addPaginationAttributes(Model model, Page<RentalOrder> orderPage, int page) {
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
    }

    private void populateFormModelAttributes(Model model, RentalOrder rentalOrder) {
        // Lấy khách hàng ABLE (lấy tất cả hoặc trang đầu tùy nhu cầu)
        Page<Customer> customerPage = customerService.getAllCustomer(0); // Lấy trang đầu
        // Lọc lại lần nữa để chắc chắn chỉ có ABLE (nếu service chưa lọc)
        List<Customer> activeCustomers = customerPage.getContent().stream()
                .filter(c -> c.getStatus() == CustomerStatus.ABLE)
                .collect(Collectors.toList());
        model.addAttribute("customers", activeCustomers);

        // Lấy xe ABLE (Available + xe đang chọn nếu là edit)
        Page<Bike> bikePage = bikeService.getAllBikes(0, Integer.MAX_VALUE); // Lấy tất cả ABLE
        List<Bike> bikeOptions = bikePage.getContent().stream()
                .filter(b -> b.getBikeStatus() == Bike.BikeStatus.Available || // Xe đang Available
                        (rentalOrder != null && rentalOrder.getBikeId() != null && b.getBikeId() == rentalOrder.getBikeId().getBikeId())) // Hoặc là xe đang được chọn trong đơn hàng edit
                .collect(Collectors.toList());
        model.addAttribute("bikes", bikeOptions);

        // Lấy KM active
        Page<Promotion> promotionsPage = promotionService.getActivePromotions(0);
        model.addAttribute("promotions", promotionsPage.getContent());

        model.addAttribute("activeMenu", "rentals"); // Đặt active menu
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

        Page<Customer> customerPage = customerService.getAllCustomer(0);
        model.addAttribute("customers", customerPage.getContent());

        Page<Bike> bikePage = bikeService.searchBikes(null, null, null, 0, Integer.MAX_VALUE);
        List<Bike> availableBikes = bikePage.getContent().stream()
                .filter(bike -> bike.getBikeStatus() == Bike.BikeStatus.Available && bike.getBikeActiveStatus() == Bike.ActiveStatus.ABLE)
                .collect(Collectors.toList());
        model.addAttribute("bikes", availableBikes);

        Page<Promotion> promotionsPage = promotionService.getActivePromotions(0);
        model.addAttribute("promotions", promotionsPage.getContent());

        model.addAttribute("activeMenu", "rentals");
        return "rental_orders/add";
    }

    // --- CẬP NHẬT PHƯƠNG THỨC POST /add ---
    @PostMapping("/add")
    public String addOrder(@ModelAttribute("rentalOrder") RentalOrder rentalOrder,
                           @RequestParam("customerId") Integer customerId, // Nhận ID khách hàng từ form
                           @RequestParam("bikeId") Integer bikeId,         // Nhận ID xe từ form
                           @RequestParam(value = "promotionId", required = false) Integer promotionId, // Nhận ID KM (tùy chọn)
                           RedirectAttributes redirectAttributes, // Dùng để gửi message sau redirect
                           Model model) { // Dùng để nạp lại form khi có lỗi
        try {
            Customer selectedCustomer = customerService.getCustomerById(customerId)
                    .filter(c -> c.getStatus() == CustomerStatus.ABLE)
                    .orElseThrow(() -> new IllegalArgumentException("Khách hàng không hợp lệ hoặc không hoạt động."));
            rentalOrder.setCustomerId(selectedCustomer);

            Bike selectedBike = bikeService.getBikeById(bikeId)
                    .filter(b -> b.getBikeStatus() == Bike.BikeStatus.Available && b.getBikeActiveStatus() == Bike.ActiveStatus.ABLE)
                    .orElseThrow(() -> new IllegalArgumentException("Xe không hợp lệ hoặc không có sẵn."));
            rentalOrder.setBikeId(selectedBike);

            Promotion appliedPromotion = null;
            if (promotionId != null && promotionId > 0) {
                appliedPromotion = promotionService.getPromotionById(promotionId)
                        .filter(p -> p.getPromotionStatus() == PromotionStatus.ABLE &&
                                !p.getPromotionStartDate().isAfter(LocalDate.now()) &&
                                !p.getPromotionEndDate().isBefore(LocalDate.now()))
                        .orElseThrow(() -> new IllegalArgumentException("Mã khuyến mãi không hợp lệ hoặc đã hết hạn."));
                rentalOrder.setPromotionId(appliedPromotion);
            } else {
                rentalOrder.setPromotionId(null);
            }

            double finalAmount = selectedBike.getBikeRentPerHour();
            if (appliedPromotion != null) {
                finalAmount = promotionService.applyPromotion(appliedPromotion.getPromotionId(), finalAmount);
            }
            rentalOrder.setRentalOrderTotalAmount(finalAmount);

            if (rentalOrder.getRentalOrderRentalDate() == null) {
                rentalOrder.setRentalOrderRentalDate(LocalDate.now());
            }
            if (rentalOrder.getRentalOrderRentalTime() == null) {
                rentalOrder.setRentalOrderRentalTime(LocalTime.now());
            }
            rentalOrder.setRentalOrderStatus(RentalStatus.ONGOING);

            rentalOrderService.addRentalOrder(rentalOrder);
            selectedBike.setBikeStatus(Bike.BikeStatus.Unavailable);
            bikeService.updateBike(selectedBike, null);
            redirectAttributes.addFlashAttribute("listSuccessMessage", "Thêm đơn thuê #" + rentalOrder.getRentalOrderId() + " thành công!");
            return "redirect:/rental-orders";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            populateFormModelAttributes(model, rentalOrder);
            return "rental_orders/add";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi hệ thống không mong muốn: " + e.getMessage());
            populateFormModelAttributes(model, rentalOrder);
            return "rental_orders/add";
        }
    }
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable int id, Model model) {
        RentalOrder rentalOrder = rentalOrderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê ID: " + id));
        model.addAttribute("rentalOrder", rentalOrder);
        populateFormModelAttributes(model, rentalOrder);
        return "rental_orders/edit";
    }

    @PostMapping("/edit")
    public String updateOrder(@ModelAttribute("rentalOrder") RentalOrder rentalOrder,
                              @RequestParam("customerId") Integer customerId,
                              @RequestParam("bikeId") Integer bikeId,
                              @RequestParam(value = "promotionId", required = false) Integer promotionId,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try {
            rentalOrderService.updateRentalOrder(rentalOrder);
            populateFormModelAttributes(model, rentalOrder);
            return "redirect:/rental-orders";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            populateFormModelAttributes(model, rentalOrder);
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
