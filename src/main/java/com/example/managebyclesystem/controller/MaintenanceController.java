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
    @GetMapping("/costAsc")
    public String getMaintenanceCostAsc(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Maintenance> maintenancePage = maintenanceService.getAllByOrderByMaintenanceCostAsc(page);
        addPaginationAttributes(model, maintenancePage, page);
        return "maintenances/list";
    }

    @GetMapping("/costDesc")
    public String getMaintenanceCostDesc(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Maintenance> maintenancePage = maintenanceService.getAllByOrderByMaintenanceCostDesc(page);
        addPaginationAttributes(model, maintenancePage, page);
        return "maintenances/list";
    }

    @GetMapping("/dateAsc")
    public String getMaintenanceDateAsc(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Maintenance> maintenancePage = maintenanceService.getAllByOrderByMaitenanceDateAsc(page);
        addPaginationAttributes(model, maintenancePage, page);
        return "maintenances/list";

    }
    @GetMapping("/dateDesc")
    public String getMaintenanceDateDesc(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Maintenance> maintenancePage = maintenanceService.getAllByOrderByMaintenanceDateDesc(page);
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
    @GetMapping("/add")
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


}