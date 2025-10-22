package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.Bike;
import com.example.managebyclesystem.service.BikeService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
// Thêm import RedirectAttributes nếu bạn muốn gửi thông báo từ POST sang GET
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bikes")
public class BikeController {

    private final BikeService bikeService;
    // Định nghĩa tên view để dễ quản lý
    private static final String SUCCESS_VIEW = "bikes/success";
    private static final String ERROR_VIEW = "bikes/error";
    private static final String LIST_VIEW = "bikes/list";
    private static final String REDIRECT_LIST_PAGED = "redirect:/bikes/listPaged"; // Chuyển hướng về trang phân trang

    @Autowired
    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    // --- Hàm xử lý POST /add ---
    @PostMapping("/add")
    public String addBike(@ModelAttribute Bike bike,
                          @RequestParam("imageFile") MultipartFile imageFile,
                          Model model, // Giữ lại Model để trả về lỗi
                          RedirectAttributes redirectAttributes) { // Thêm RedirectAttributes
        try {
            bikeService.addBike(bike, imageFile);
            // Gửi thông báo thành công qua Flash Attribute
            redirectAttributes.addFlashAttribute("successMessage", "Thêm xe đạp '" + bike.getBikeName() + "' thành công!");
            return "redirect:/bikes/listPaged"; // Chuyển hướng về danh sách phân trang
            // return "redirect:/bikes/success"; // Hoặc nếu vẫn muốn trang success riêng
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi thêm xe: " + e.getMessage());
            // Khi lỗi, trả về view error trực tiếp
            // Cần đảm bảo view error có đủ các biến cần thiết nếu nó dùng layout chung
            model.addAttribute("activeMenu", "bikes"); // Thêm nếu trang lỗi dùng layout
            return ERROR_VIEW;
        }
    }

    // --- Hàm xử lý POST /update ---
    @PostMapping("/update")
    public String updateBike(@ModelAttribute Bike bike,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             Model model, // Giữ lại Model để trả về lỗi
                             RedirectAttributes redirectAttributes) { // Thêm RedirectAttributes
        try {
            bikeService.updateBike(bike, imageFile);
            // Gửi thông báo thành công qua Flash Attribute
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật xe đạp ID #" + bike.getBikeId() + " thành công!");
            return REDIRECT_LIST_PAGED; // Chuyển hướng về danh sách phân trang
            // return "redirect:/bikes/success"; // Hoặc nếu vẫn muốn trang success riêng
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi cập nhật xe: " + e.getMessage());
            // Khi lỗi, trả về view error trực tiếp
            model.addAttribute("activeMenu", "bikes"); // Thêm nếu trang lỗi dùng layout
            return ERROR_VIEW;
        }
    }

    // --- Hàm xử lý POST /delete/{id} ---
    @PostMapping("/delete/{id}")
    public String deleteBike(@PathVariable int id,
                             RedirectAttributes redirectAttributes) { // Dùng RedirectAttributes
        try {
            bikeService.disableBike(id);
            System.out.println("🗑Đã vô hiệu hóa xe đạp có ID: " + id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã vô hiệu hóa xe đạp ID #" + id + ".");
            return REDIRECT_LIST_PAGED; // Chuyển hướng về danh sách phân trang
        } catch (Exception e) {
            System.err.println("Lỗi khi vô hiệu hóa xe đạp: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa xe: " + e.getMessage());
            return REDIRECT_LIST_PAGED; // Chuyển hướng về danh sách phân trang ngay cả khi lỗi
            // Hoặc return "redirect:/bikes/error"; nếu muốn trang lỗi riêng
        }
    }

    // === THÊM PHƯƠNG THỨC NÀY ===
    /**
     * Xử lý yêu cầu GET /bikes/success để hiển thị trang thành công.
     */
    @GetMapping("/success")
    public String showSuccessPage(Model model) {
        // Thêm activeMenu nếu trang success dùng layout chung
        model.addAttribute("activeMenu", "bikes");
        return SUCCESS_VIEW; // Trả về templates/bikes/success.html
    }

    // === THÊM/SỬA PHƯƠNG THỨC NÀY ===
    /**
     * Xử lý yêu cầu GET /bikes/error để hiển thị trang lỗi.
     */
    @GetMapping("/error")
    public String showErrorPage(Model model) {
        // Thêm activeMenu nếu trang lỗi dùng layout chung
        model.addAttribute("activeMenu", "bikes");
        // Controller có thể đã thêm errorMessage vào Model trước khi trả về view này
        return ERROR_VIEW; // Trả về templates/bikes/error.html
    }


    // --- Các phương thức GET /listPaged và /search ---
    // Đảm bảo các phương thức này thêm activeMenu và newBike
    @GetMapping("/listPaged")
    public String listPagedBikes(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "bikeName") String sortBy,
                                 @RequestParam(defaultValue = "asc") String sortDir,
                                 Model model) {
        // ... (code lấy dữ liệu phân trang) ...
        Page<Bike> bikePage = bikeService.getPagedAndSortedBikes(page, size, sortBy, sortDir);
        model.addAttribute("bikes", bikePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bikePage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        String reverseSortDir = sortDir.equalsIgnoreCase("asc") ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);

        // Thêm các thuộc tính cần thiết
        model.addAttribute("activeMenu", "bikes");
        if (!model.containsAttribute("newBike")) { // Chỉ thêm nếu chưa có (tránh ghi đè khi redirect lỗi từ POST /add)
            model.addAttribute("newBike", new Bike());
        }

        return LIST_VIEW; // Trả về templates/bikes/list.html
    }

    @GetMapping("/search")
    public String searchBikes(@RequestParam(required = false) String name,
                              @RequestParam(required = false) Bike.BikeType type,
                              @RequestParam(required = false) String location,
                              Model model) {
        try {
            var bikes = bikeService.searchBikes(name, type, location);
            model.addAttribute("bikes", bikes);
            model.addAttribute("searchName", name);
            model.addAttribute("searchType", type != null ? type.name() : null);
            model.addAttribute("searchLocation", location);

            // Thêm các thuộc tính cần thiết
            model.addAttribute("activeMenu", "bikes");
            if (!model.containsAttribute("newBike")) {
                model.addAttribute("newBike", new Bike());
            }
            // Thêm phân trang giả
            model.addAttribute("totalPages", 1);
            model.addAttribute("currentPage", 0);
            model.addAttribute("sortBy", "bikeName");
            model.addAttribute("sortDir", "asc");

            return LIST_VIEW; // Trả về templates/bikes/list.html
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("activeMenu", "bikes"); // Thêm cả khi lỗi
            if (!model.containsAttribute("newBike")) {
                model.addAttribute("newBike", new Bike());
            }
            // Thêm phân trang giả khi lỗi
            model.addAttribute("totalPages", 0);
            model.addAttribute("currentPage", 0);
            model.addAttribute("bikes", List.of()); // Trả về danh sách rỗng
            return LIST_VIEW; // Vẫn trả về trang list để hiển thị lỗi
            // return ERROR_VIEW; // Hoặc trả về trang lỗi riêng
        }
    }
}