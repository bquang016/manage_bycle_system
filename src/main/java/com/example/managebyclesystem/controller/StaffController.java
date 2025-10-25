package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.Staff;
import com.example.managebyclesystem.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staffs")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public String listStaffs(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "staffName") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model
    ) {
        Page<Staff> staffPage = staffService.getPaginatedAndSortedStaffs(pageNo, pageSize, sortField, sortDir);

        model.addAttribute("staffs", staffPage.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", staffPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "staffs/list";
    }


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("staff", new Staff());
        return "staffs/add";
    }


    @PostMapping("/add")
    public String addStaff(@ModelAttribute Staff staff, Model model) {
        try {
            staffService.addStaff(staff);
            model.addAttribute("message", "Thêm nhân viên thành công!");
            return "redirect:/staffs";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "staffs/add";
        }
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Staff staff = staffService.getStaffById(id);
        model.addAttribute("staff", staff);
        return "staffs/edit";
    }


    @PostMapping("/edit/{id}")
    public String updateStaff(@PathVariable("id") int id, @ModelAttribute Staff updatedStaff, Model model) {
        try {
            updatedStaff.setStaffId(id);
            staffService.updateStaff(updatedStaff);
            model.addAttribute("message", "Cập nhật nhân viên thành công!");
            return "redirect:/staffs";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "staffs/edit";
        }
    }


    @GetMapping("/delete/{id}")
    public String deleteStaff(@PathVariable("id") int id, Model model) {
        try {
            staffService.deleteStaff(id);
            model.addAttribute("message", "Đã vô hiệu hóa nhân viên thành công!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/staffs";
    }


    @GetMapping("/search")
    public String searchStaffs(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String position,
                               Model model) {
        try {
            var staffs = staffService.searchStaffs(keyword, position);
            model.addAttribute("staffs", staffs);
            model.addAttribute("message", "Tìm thấy " + staffs.size() + " nhân viên phù hợp.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "staffs/list";
    }
}
