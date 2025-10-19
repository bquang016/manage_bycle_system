package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staffs")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping("/add")
    public String addStaff(
            @RequestParam("staffName") String staffName,
            @RequestParam("staffPosition") String position,
            @RequestParam("staffSalary") double salary,
            @RequestParam(value = "staffShift", required = false) String shift,
            @RequestParam(value = "staffRoles", defaultValue = "false") boolean roles
    ) {
        try {
            staffService.addStaff(staffName, position, salary, shift, roles);
            return "redirect:/staffs/success";
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm nhân viên: " + e.getMessage());
            return "redirect:/staffs/error";
        }
    }
}
