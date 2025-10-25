package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.Maintenance;
import com.example.managebyclesystem.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @GetMapping("/costAsc")
    public String getMaintenanceCostAsc(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Maintenance> maintenancePage = maintenanceService.getAllByOrderByMaintenanceCostAsc(page);
        addPaginationAttributes(model, maintenancePage, page);
        return "maintenance/list";
    }

    @GetMapping("/costDesc")
    public String getMaintenanceCostDesc(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Maintenance> maintenancePage = maintenanceService.getAllByOrderByMaintenanceCostDesc(page);
        addPaginationAttributes(model, maintenancePage, page);
        return "maintenance/list";
    }

    @GetMapping("/dateAsc")
    public String getMaintenanceDateAsc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Maintenance> maintenancePage = maintenanceService.getAllByOrderByMaitenanceDateAsc(page);
        addPaginationAttributes(model, maintenancePage, page);
        return "maintenance/list";

    }
    @GetMapping("/dateDesc")
    public String getMaintenanceDateDesc(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Maintenance> maintenancePage = maintenanceService.getAllByOrderByMaintenanceDateDesc(page);
        addPaginationAttributes(model, maintenancePage, page);
        return "maintenance/list";
    }

    private void addPaginationAttributes(Model model, Page<Maintenance> maintenancePage, int page) {
        model.addAttribute("maintenance", maintenancePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", maintenancePage.getTotalPages());
    }
}