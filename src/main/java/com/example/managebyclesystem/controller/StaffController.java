package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.Staff;
import com.example.managebyclesystem.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@RequestMapping("/api/staffs")
@CrossOrigin(origins = "*")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }


    @PostMapping("/add")
    public ResponseEntity<String> addStaff(@RequestBody Staff staff) {
        try {
            staffService.addStaff(
                    staff.getStaffName(),
                    staff.getStaffPosition().toString(),
                    staff.getStaffSalary(),
                    staff.getStaffShift().toString(),
                    staff.isStaffRoles()
            );
            return ResponseEntity.ok("Thêm nhân viên thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi thêm nhân viên: " + e.getMessage());
        }
    }



    @GetMapping("/list")
    public List<Staff> listStaffs() {
        var staffs = staffService.getAllActiveStaffs();
        System.out.println("Danh sách nhân viên đang hoạt động:");
        staffs.forEach(staff -> System.out.println(
                staff.getStaffId() + " - " +
                        staff.getStaffName() + " - " +
                        staff.getStaffPosition() + " - " +
                        staff.getStaffSalary() + " - " +
                        staff.getStaffShift()
        ));
        return staffs;
    }
}
