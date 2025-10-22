package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.Bike;
import com.example.managebyclesystem.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bikes")
public class BikeController {

    private final BikeService bikeService;

    @Autowired
    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @PostMapping("/add")
    public String addBike(@ModelAttribute Bike bike, Model model) {
        try {
            bikeService.addBike(bike);
            System.out.println("Thêm xe đạp thành công: " + bike.getBikeName());
            return "redirect:/bikes/success"; // Dùng redirect sau khi POST
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm xe đạp: " + e.getMessage());
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
    public String updateBike(@ModelAttribute Bike bike, Model model) {
        try {
            bikeService.updateBike(bike);
            return "redirect:/bikes/success";
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật xe đạp: " + e.getMessage());
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
}