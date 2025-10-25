package com.example.managebyclesystem.service;

import com.example.managebyclesystem.model.Staff;
import com.example.managebyclesystem.model.Staff.StaffPosition;
import com.example.managebyclesystem.model.Staff.StaffShift;
import com.example.managebyclesystem.model.Staff.StaffStatus;
import com.example.managebyclesystem.model.StaffRole;
import com.example.managebyclesystem.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public void addStaff(Staff staff) {
        if (staff.getStaffName() == null || staff.getStaffName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        }

        if (staff.getUsername() == null || staff.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống");
        }

        if (staff.getPassword() == null || staff.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }

        if (staff.getStaffSalary() <= 0) {
            throw new IllegalArgumentException("Lương phải lớn hơn 0");
        }

        if (staff.getStaffPosition() == null) {
            throw new IllegalArgumentException("Chức vụ không hợp lệ");
        }

        if (staff.getStaffShift() == null) {
            throw new IllegalArgumentException("Ca làm việc không hợp lệ");
        }

        // check uname ton tai
        if (staffRepository.findAll().stream()
                .anyMatch(s -> s.getUsername().equalsIgnoreCase(staff.getUsername()))) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }

        // mặc định able, user
        staff.setStaffStatus(StaffStatus.Able);
        if (staff.getRole() == null) {
            staff.setRole(StaffRole.ROLE_USER);
        }

        staffRepository.save(staff);
        System.out.println("Thêm nhân viên thành công: " + staff.getStaffName());
    }

    // list nv able
    public List<Staff> getAllActiveStaffs() {
        return staffRepository.findByStaffStatus(StaffStatus.Able);
    }


    public Staff getStaffById(int id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + id));
    }


    public void updateStaff(Staff updatedStaff) {
        Staff existingStaff = getStaffById(updatedStaff.getStaffId());

        if (existingStaff.getStaffStatus() == StaffStatus.Disable) {
            throw new RuntimeException("Không thể sửa vì nhân viên đã bị vô hiệu hóa.");
        }

        existingStaff.setStaffName(updatedStaff.getStaffName().trim());
        existingStaff.setStaffPosition(updatedStaff.getStaffPosition());
        existingStaff.setStaffSalary(updatedStaff.getStaffSalary());
        existingStaff.setStaffShift(updatedStaff.getStaffShift());
        existingStaff.setStaffStatus(updatedStaff.getStaffStatus());

        // update uname
        if (updatedStaff.getUsername() != null && !updatedStaff.getUsername().trim().isEmpty()) {
            existingStaff.setUsername(updatedStaff.getUsername().trim());
        }

        // update mk
        if (updatedStaff.getPassword() != null && !updatedStaff.getPassword().isBlank()) {
            existingStaff.setPassword(updatedStaff.getPassword());
        }

        // update role
        if (updatedStaff.getRole() != null) {
            existingStaff.setRole(updatedStaff.getRole());
        }

        staffRepository.save(existingStaff);
        System.out.println("Cập nhật nhân viên thành công: " + existingStaff.getStaffName());
    }


    public void deleteStaff(int staffId) {
        Staff staff = getStaffById(staffId);
        // check disable
        if (staff.getStaffStatus() == StaffStatus.Disable) {
            throw new RuntimeException("Nhân viên này đã bị vô hiệu hóa trước đó.");
        }
        // set dis
        staff.setStaffStatus(StaffStatus.Disable);
        staffRepository.save(staff);

        System.out.println("Đã vô hiệu hóa nhân viên ID = " + staffId);
    }


    public List<Staff> searchStaffs(String keyword, String position) {
        if ((keyword == null || keyword.trim().isEmpty()) &&
                (position == null || position.trim().isEmpty())) {
            throw new IllegalArgumentException("Vui lòng nhập tên hoặc chức vụ.");
        }

        String formattedKeyword = "%" + keyword.trim().toLowerCase() + "%";
        StaffPosition staffPosition = null;

        if (position != null && !position.trim().isEmpty()) {
            try {
                staffPosition = StaffPosition.valueOf(position.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Chức vụ không hợp lệ. Chỉ được: MANAGER, STAFF, SECURITY, MAINTENANCE");
            }
        }

        return staffRepository.searchStaffs(formattedKeyword, staffPosition);
    }

    // phân trang + sort
    public Page<Staff> getPaginatedAndSortedStaffs(int pageNo, int pageSize, String sortField, String sortDir) {
        int pageIndex = pageNo - 1;

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        return staffRepository.findByStaffStatusNot(StaffStatus.Disable, pageable);
    }
}
