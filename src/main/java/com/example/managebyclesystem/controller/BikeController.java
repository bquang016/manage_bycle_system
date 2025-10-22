package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.Bike;
import com.example.managebyclesystem.service.BikeService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/bikes")
public class BikeController {

    private final BikeService bikeService;

    @Autowired
    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @PostMapping("/add")
    public String addBike(@ModelAttribute Bike bike,
                          @RequestParam("imageFile") MultipartFile imageFile,
                          Model model) {
        try {
            bikeService.addBike(bike, imageFile);
            return "redirect:/bikes/success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "bikes/error";
        }
    }

    @GetMapping("/list")
    public String listBikes(Model model) {
        var bikes = bikeService.getAllBikes();
        model.addAttribute("bikes", bikes);
        System.out.println("DANH SÁCH TOÀN BỘ XE ĐẠP");
        bikes.forEach(bike -> System.out.println(
                "ID: " + bike.getBikeId() + " , " +
                        "Tên: " + bike.getBikeName() + " , " +
                        "Loại: " + bike.getBikeType()
        ));

        return "bikes/list";
    }

    @PostMapping("/update")
    public String updateBike(@ModelAttribute Bike bike,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             Model model) {
        try {
            bikeService.updateBike(bike, imageFile);
            return "redirect:/bikes/success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "bikes/error";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteBike(@PathVariable int id) {
        try {
            bikeService.disableBike(id);
            System.out.println("🗑Đã vô hiệu hóa xe đạp có ID: " + id);
            return "redirect:/bikes/list";
        } catch (Exception e) {
            System.err.println("Lỗi khi vô hiệu hóa xe đạp: " + e.getMessage());
            return "redirect:/bikes/error";
        }
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
            model.addAttribute("searchType", type);
            model.addAttribute("searchLocation", location);

            System.out.println("KẾT QUẢ TÌM KIẾM (chỉ xe ABLE):");
            bikes.forEach(bike -> System.out.println(
                    "ID: " + bike.getBikeId() + " , " +
                            "Tên: " + bike.getBikeName() + " , " +
                            "Loại: " + bike.getBikeType() + " , " +
                            "Vị trí: " + bike.getBikeLocation()
            ));

            return "bikes/list";
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm kiếm xe đạp: " + e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "bikes/error";
        }
    }
    @GetMapping("/listPaged")
    public String listPagedBikes(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "bikeName") String sortBy,
                                 @RequestParam(defaultValue = "asc") String sortDir,
                                 Model model) {

        Page<Bike> bikePage = bikeService.getPagedAndSortedBikes(page, size, sortBy, sortDir);

        model.addAttribute("bikes", bikePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bikePage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        String reverseSortDir = sortDir.equalsIgnoreCase("asc") ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);

        System.out.println("Trang: " + (page + 1) + "/" + bikePage.getTotalPages() +
                " | Sắp xếp theo: " + sortBy + " (" + sortDir + ")");

        return "bikes/list";
    }
}