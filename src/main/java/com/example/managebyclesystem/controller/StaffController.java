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

    // Thêm nv
    @PostMapping("/staffs/add")
    public String addStaff(@ModelAttribute Staff staff, Model model) {
        try {
            staffService.addStaff(staff);
            model.addAttribute("message", "Thêm nhân viên thành công: " + staff.getStaffName());
            System.out.println("Đã thêm nhân viên: " + staff.getStaffName());
            return "staff_success";
        }catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            System.out.println("Lỗi khi thêm nhân viên: " + e.getMessage());
            return "staff_error";
        }
    }

    // Danh sách nv able
    @GetMapping("/list")
    public String listStaffs(Model model) {
        var staffs = staffService.getAllActiveStaffs();
        model.addAttribute("staffs", staffs); // Gửi nv sang view
        System.out.println("Danh sách nhân viên: " + staffs.size() + " nhân viên.");
        return "staff_list";
    }

    @PostMapping("/update")
    public String updateStaff(@ModelAttribute Staff staff, Model model) {
        try {
            staffService.updateStaff(staff);
            model.addAttribute("message", "Cập nhật nhân viên thành công: " + staff.getStaffName());
            System.out.println("Đã cập nhật nhân viên: " + staff.getStaffName());
            return "staff_success";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi cập nhật nhân viên: " + e.getMessage());
            System.out.println("Lỗi khi cập nhật nhân viên: " + e.getMessage());
            return "staff_error";
        }
    }


    // Tìm theo tên or chức vụ
    @PostMapping("/search")
    public String searchStaffs(@ModelAttribute Staff staff, Model model) {
        try {
            String keyword = staff.getStaffName();
            String position = (staff.getStaffPosition() != null) ? staff.getStaffPosition().name() : "";

            var staffs = staffService.searchStaffs(keyword, position);

            model.addAttribute("staffs", staffs);
            model.addAttribute("message", "Tìm thấy " + staffs.size() + " nhân viên.");

            System.out.println("Tìm thấy " + staffs.size() + " nhân viên phù hợp: " + keyword);
            return "staff_search_result";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            System.out.println("Lỗi khi tìm kiếm nhân viên: " + e.getMessage());
            return "staff_error";
        }
    }

    @PostMapping("/delete")
    public String deleteStaff(@RequestParam("staffId") int staffId, Model model) {
        try {
            staffService.deleteStaff(staffId);
            model.addAttribute("message", "Xóa nhân viên thành công. ID = " + staffId);
            System.out.println("Đã xóa nhân viên ID = " + staffId);
            return "staff_success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            System.out.println("Lỗi khi xóa nhân viên: " + e.getMessage());
            return "staff_error";
        }
    }

    // sắp xêp + phân trang
    @GetMapping("/page")
    public String getStaffPage(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "staffName") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model
    ) {
        Page<Staff> page = staffService.getPaginatedAndSortedStaffs(pageNo, pageSize, sortField, sortDir);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("staffs", page.getContent());

        System.out.println("Trang " + pageNo + " / " + page.getTotalPages());
        return "staff_page_list";
    }


}
