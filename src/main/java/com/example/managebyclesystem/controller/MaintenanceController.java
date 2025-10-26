package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.Maintenance;
import com.example.managebyclesystem.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/maintenances")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping
    public String getAllMaintenance(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Maintenance> maintenancePage = maintenanceService.getAllMaintenance(page);
        addPaginationAttributes(model, maintenancePage, page);
        return "maintenances/list";
    }
    @GetMapping("/sort/cost")
    public String getMaintenanceSortedByCost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "asc") String order,
            Model model
    ) {
        Page<Maintenance> maintenancePage;

        if ("desc".equalsIgnoreCase(order)) {
            maintenancePage = maintenanceService.getAllByOrderByMaintenanceCostDesc(page);
        } else {
            maintenancePage = maintenanceService.getAllByOrderByMaintenanceCostAsc(page);
        }

        addPaginationAttributes(model, maintenancePage, page);


        return "maintenances/list";
    }


    @GetMapping("/sort/date")
    public String getMaintenanceSortedByDate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "asc") String order,
            Model model
    ) {
        Page<Maintenance> maintenancePage;

        if ("desc".equalsIgnoreCase(order)) {
            maintenancePage = maintenanceService.getAllByOrderByMaintenanceDateDesc(page);
        } else {
            maintenancePage = maintenanceService.getAllByOrderByMaitenanceDateAsc(page);
        }

        addPaginationAttributes(model, maintenancePage, page);

        return "maintenances/list";
    }


    private void addPaginationAttributes(Model model, Page<Maintenance> maintenancePage, int page) {
        model.addAttribute("maintenance", maintenancePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", maintenancePage.getTotalPages());
    }

    @GetMapping("/add")
    public String showAddForm(Model model){
        model.addAttribute("maintenance", new Maintenance());
        return "maintenances/add";
    }
    @PostMapping("/add")
    public String addMaintenance(@ModelAttribute("maintenance") Maintenance maintenance, Model model){
        try {
            maintenanceService.addMaintenance(maintenance);
            return "redirect:/maintenances";
        }catch (IllegalArgumentException e){
            model.addAttribute("errorMesage", e.getMessage());
            return "maintenances/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model){
        Maintenance maintenance = maintenanceService.getMaintenanceById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bảo trì id:"+id));
        model.addAttribute("maintenance", maintenance);
        return "maintenances/edit";
    }
    @PostMapping("/edit/{id}")
    public String updateMaintenance(@PathVariable int id, @ModelAttribute("maintenace") Maintenance newMaintenace, Model model) {
        try {
            maintenanceService.updateMaintenance(id, newMaintenace);
            return "redirect:/maintenances";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "maintenances/edit";
        }
    }

    @GetMapping("delete/{id}")
    public String deleteMaintenance(@PathVariable int id){
        maintenanceService.deleteMaintenance(id);
        return "redirect:/maintenances";
    }

    @GetMapping("/search/bikeId")
    public String searchByBikeId(@RequestParam String bikeId, @RequestParam(defaultValue = "0") int page, Model model) {
        Page<Maintenance> maintenancePage = maintenanceService.getMaintenanceByBikeId(bikeId, page);
        return prepareSearchModel(model, maintenancePage, page, "bikeId", bikeId);
    }

    @GetMapping("/search/date")
    public String searchByDate(@RequestParam String date, @RequestParam(defaultValue = "0") int page, Model model) {
        Page<Maintenance> maintenancePage = maintenanceService.getMaintenanceByDate(date, page);
        return prepareSearchModel(model, maintenancePage, page, "date", date);
    }

    private String prepareSearchModel(Model model, Page<Maintenance> maintenancePage, int page, String searchType, String keyword) {
        model.addAttribute("maintenances", maintenancePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", maintenancePage.getTotalPages());
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        return "maintenances/list";
    }

}