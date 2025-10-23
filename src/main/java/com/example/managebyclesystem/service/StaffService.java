package com.example.managebyclesystem.service;

import com.example.managebyclesystem.model.Staff;
import com.example.managebyclesystem.model.Staff.StaffPosition;
import com.example.managebyclesystem.model.Staff.StaffShift;
import com.example.managebyclesystem.model.Staff.StaffStatus;
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

    // thêm nv
    public void addStaff(Staff staff) {
        if (staff.getStaffName() == null || staff.getStaffName().trim().isEmpty())
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        if (staff.getStaffSalary() <= 0)
            throw new IllegalArgumentException("Lương phải lớn hơn 0");
        if (staff.getStaffPosition() == null)
            throw new IllegalArgumentException("Chức vụ không hợp lệ");
        if (staff.getStaffShift() == null)
            throw new IllegalArgumentException("Ca làm việc không hợp lệ");

        staff.setStaffStatus(StaffStatus.Able);
        staffRepository.save(staff);
        System.out.println("Thêm nhân viên: " + staff.getStaffName());
    }

    // find nv able
    public List<Staff> getAllActiveStaffs() {
        return staffRepository.findByStaffStatus(StaffStatus.Able);
    }

    // find id forrm sửa
    public Staff getStaffById(int staffId) {
        return staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + staffId));
    }


    public void updateStaff(Staff updatedStaff) {
        Staff existingStaff = getStaffById(updatedStaff.getStaffId());

        if (existingStaff.getStaffStatus() == StaffStatus.Disable)
            throw new RuntimeException("Không thể sửa vì nhân viên là Disable.");

        existingStaff.setStaffName(updatedStaff.getStaffName().trim());
        existingStaff.setStaffPosition(updatedStaff.getStaffPosition());
        existingStaff.setStaffSalary(updatedStaff.getStaffSalary());
        existingStaff.setStaffShift(updatedStaff.getStaffShift());
        existingStaff.setStaffRoles(updatedStaff.isStaffRoles());

        staffRepository.save(existingStaff);
        System.out.println("Cập nhật nhân viên thành công: " + existingStaff.getStaffName());
    }


    public void deleteStaff(int staffId) {
        Staff staff = getStaffById(staffId);
        // check disable
        if (staff.getStaffStatus() == StaffStatus.Disable)
            throw new RuntimeException("Nhân viên (ID=" + staffId + ") đã bị vô hiệu hóa.");

        staff.setStaffStatus(StaffStatus.Disable);
        staffRepository.save(staff);
        System.out.println("🗑️ Đã xóa nhân viên: " + staff.getStaffName());
    }

    // search
    public List<Staff> searchStaffs(String keyword, String position) {
        if ((keyword == null || keyword.trim().isEmpty()) &&
                (position == null || position.trim().isEmpty()))
            throw new IllegalArgumentException("Vui lòng nhập tên hoặc chức vụ.");

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
