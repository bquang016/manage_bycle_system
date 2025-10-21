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

    // Thêm nv
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
            System.out.println("Thêm nhân viên thành công: " + staffName);
            return "redirect:/staffs/success";
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm nhân viên: " + e.getMessage());
            return "redirect:/staffs/error";
        }
    }

    // Lấy nv able
    @GetMapping("/list")
    public String listStaffs() {
        var staffs = staffService.getAllActiveStaffs();
        System.out.println("Danh sách nhân viên đang hoạt động:");
        staffs.forEach(staff -> System.out.println(
                staff.getStaffId() + " - " +
                        staff.getStaffName() + " - " +
                        staff.getStaffPosition() + " - " +
                        staff.getStaffSalary() + " - " +
                        staff.getStaffShift()
        ));
        return "staffs/list";
    }
}
