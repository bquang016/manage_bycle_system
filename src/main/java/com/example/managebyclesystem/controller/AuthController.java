package com.example.managebyclesystem.controller;

import com.example.managebyclesystem.model.StaffRole; // Sử dụng StaffRole từ model
import com.example.managebyclesystem.model.Staff;
import com.example.managebyclesystem.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Import để xử lý lỗi validation
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AuthController {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginPage(Model model,
                                @RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                @ModelAttribute("successMessage") String successMessage // Nhận flash attribute từ redirect
    ) {
        if (error != null) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Bạn đã đăng xuất thành công.");
        }
        if (successMessage != null && !successMessage.isEmpty()){
            model.addAttribute("successMessage", successMessage);
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("staff")) {
            model.addAttribute("staff", new Staff());
        }
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @ModelAttribute("staff") Staff staff,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {

        if (staff.getUsername() == null || staff.getUsername().trim().isEmpty()) {
            bindingResult.rejectValue("username", "error.staff", "Tên đăng nhập không được để trống");
        } else if (staffRepository.existsByUsername(staff.getUsername())) { //
            bindingResult.rejectValue("username", "error.staff", "Tên đăng nhập đã tồn tại");
        }
        if (staff.getPassword() == null || staff.getPassword().isEmpty()) {
            bindingResult.rejectValue("password", "error.staff", "Mật khẩu không được để trống");
        }
        if (staff.getStaffName() == null || staff.getStaffName().trim().isEmpty()) {
            bindingResult.rejectValue("staffName", "error.staff", "Họ tên không được để trống");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            // Mã hóa mật khẩu
            staff.setPassword(passwordEncoder.encode(staff.getPassword())); //

            // Đặt vai trò mặc định là USER
            staff.setRole(StaffRole.ROLE_USER); //

            // Đặt trạng thái mặc định là Able
            staff.setStaffStatus(Staff.StaffStatus.Able); //

            if (staff.getStaffPosition() == null) staff.setStaffPosition(Staff.StaffPosition.STAFF);
            if (staff.getStaffShift() == null) staff.setStaffShift(Staff.StaffShift.MORNING);

            staffRepository.save(staff);

            redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn trong quá trình đăng ký.");
            return "register";
        }
    }
}