package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.Bike;
import com.example.managebyclesystem.model.Bike.BikeType;
import com.example.managebyclesystem.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    private static final int PAGE_SIZE = 10; // có thể điều chỉnh theo nhu cầu

    @GetMapping
    public String getAllBikes(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Bike> bikePage = bikeService.getAllBikes(page, PAGE_SIZE);
        addPaginationAttributes(model, bikePage, page);
        return "bikes/list";
    }

    @GetMapping("/nameAsc")
    public String getNameAsc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Bike> bikePage = bikeService.getAllByOrderByNameAsc(page, PAGE_SIZE);
        addPaginationAttributes(model, bikePage, page);
        return "bikes/list";
    }

    @GetMapping("/nameDesc")
    public String getNameDesc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Bike> bikePage = bikeService.getAllByOrderByNameDesc(page, PAGE_SIZE);
        addPaginationAttributes(model, bikePage, page);
        return "bikes/list";
    }

    @GetMapping("/rentAsc")
    public String getRentAsc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Bike> bikePage = bikeService.getAllByOrderByPriceAsc(page, PAGE_SIZE);
        addPaginationAttributes(model, bikePage, page);
        return "bikes/list";
    }

    @GetMapping("/rentDesc")
    public String getRentDesc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Bike> bikePage = bikeService.getAllByOrderByPriceDesc(page, PAGE_SIZE);
        addPaginationAttributes(model, bikePage, page);
        return "bikes/list";
    }

    private void addPaginationAttributes(Model model, Page<Bike> bikePage, int page) {
        model.addAttribute("bikes", bikePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bikePage.getTotalPages());
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("bike", new Bike());
        return "bikes/add";
    }

    @PostMapping("/add")
    public String addBike(@ModelAttribute("bike") Bike bike,
                          @RequestParam("imageFile") MultipartFile imageFile,
                          Model model) {
        try {
            bikeService.addBike(bike, imageFile);
            return "redirect:/bikes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "bikes/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable int id, Model model) {
        Bike bike = bikeService.getBikeById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe đạp ID: " + id));
        model.addAttribute("bike", bike);
        return "bikes/edit";
    }

    @PostMapping("/edit")
    public String updateBike(@ModelAttribute("bike") Bike bike,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             Model model) {
        try {
            bikeService.updateBike(bike, imageFile);
            return "redirect:/bikes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "bikes/edit";
        }
    }

    @GetMapping("/search")
    public String searchBikes(@RequestParam(required = false) String name,
                              @RequestParam(required = false) BikeType type,
                              @RequestParam(required = false) String location,
                              @RequestParam(defaultValue = "0") int page,
                              Model model) {
        Page<Bike> bikePage = bikeService.searchBikes(name, type, location, page, PAGE_SIZE);
        addPaginationAttributes(model, bikePage, page);
        model.addAttribute("searchName", name);
        model.addAttribute("searchType", type);
        model.addAttribute("searchLocation", location);
        return "bikes/list";
    }

    @GetMapping("/delete/{id}")
    public String disableBike(@PathVariable int id) {
        bikeService.disableBike(id);
        return "redirect:/bikes";
    }

}