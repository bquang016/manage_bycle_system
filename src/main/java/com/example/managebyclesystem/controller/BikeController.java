package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public String addBike(
            @RequestParam("bikeImage") String bikeImage,
            @RequestParam("bikeType") String bikeType,
            @RequestParam("bikeName") String bikeName,
            @RequestParam("hourlyRate") double hourlyRate,
            @RequestParam("bikeLocation") String bikeLocation,
            @RequestParam("bikeStatus") String bikeStatus
    ) {
        try {
            bikeService.addBike(bikeImage, bikeType, bikeName, hourlyRate, bikeLocation, bikeStatus);
            System.out.println("Thêm xe đạp thành công: " + bikeName);
            return "redirect:/bikes/success";
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm xe đạp: " + e.getMessage());
            return "redirect:/bikes/error";
        }
    }
    @GetMapping("/list")
    public String listBikes() {
        var bikes = bikeService.getAllBikes();
        System.out.println("===== DANH SÁCH TOÀN BỘ XE ĐẠP =====");
        bikes.forEach(bike -> System.out.println(
                "ID: " + bike.getBikeId() + " | " +
                        "Ảnh: " + bike.getBikeImage() + " | " +
                        "Loại: " + bike.getBikeType() + " | " +
                        "Tên: " + bike.getBikeName() + " | " +
                        "Giá thuê/giờ: " + bike.getBikeRentPerHour() + " | " +
                        "Trạng thái: " + bike.getBikeStatus() + " | " +
                        "Vị trí: " + bike.getBikeLocation()
        ));
        return "bikes/list";
    }
}

