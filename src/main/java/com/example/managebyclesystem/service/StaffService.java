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

    public void addStaff(Staff staff) {

        if (staff.getStaffName() == null || staff.getStaffName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        }

        if (staff.getStaffSalary() <= 0) {
            throw new IllegalArgumentException("Lương phải lớn hơn 0");
        }
        // check chức vụ
        if (staff.getStaffPosition() == null) {
            throw new IllegalArgumentException("Chức vụ không hợp lệ (MANAGER, STAFF, SECURITY, MAINTENANCE)");
        }
        // check ca làm
        if (staff.getStaffShift() == null) {
            throw new IllegalArgumentException("Ca làm việc không hợp lệ (MORNING, AFTERNOON, EVENING, FULLDAY)");
        }
        staff.setStaffStatus(Staff.StaffStatus.Able); // mặc định able

        staffRepository.save(staff);
        System.out.println("Thêm nhân viên thành công: " + staff.getStaffName());
    }


    // Lấy danh sách nv able
    public List<Staff> getAllActiveStaffs() {
        return staffRepository.findByStaffStatus(Staff.StaffStatus.Able);
    }


    // Cập nhật nv
    public void updateStaff(Staff updatedStaff) {
        Staff existingStaff = staffRepository.findById(updatedStaff.getStaffId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + updatedStaff.getStaffId()));

        // Check disable
        if (existingStaff.getStaffStatus() == StaffStatus.Disable) {
            throw new RuntimeException("Không thể sửa vì nhân viên đang ở trạng thái Disable.");
        }

        if (updatedStaff.getStaffName() == null || updatedStaff.getStaffName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        }

        StaffPosition staffPosition = updatedStaff.getStaffPosition();
        if (staffPosition == null) {
            throw new IllegalArgumentException("Chức vụ không hợp lệ. Chỉ được: MANAGER, STAFF, SECURITY, MAINTENANCE");
        }

        StaffShift staffShift = updatedStaff.getStaffShift();
        if (staffShift == null) {
            throw new IllegalArgumentException("Ca làm việc không hợp lệ. Chỉ được: MORNING, AFTERNOON, EVENING, FULLDAY");
        }

        // cập nhật thtin mới
        existingStaff.setStaffName(updatedStaff.getStaffName().trim());
        existingStaff.setStaffPosition(staffPosition);
        existingStaff.setStaffSalary(updatedStaff.getStaffSalary());
        existingStaff.setStaffShift(staffShift);
        existingStaff.setStaffRoles(updatedStaff.isStaffRoles());

        staffRepository.save(existingStaff);
        System.out.println("Cập nhật nhân viên thành công: " + existingStaff.getStaffName());
    }


    // Tìm theo tên or chức vụ
    public List<Staff> searchStaffs(String keyword, String position) {
        if ((keyword == null || keyword.trim().isEmpty()) && (position == null || position.trim().isEmpty())) {
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

    // xóa nv
    public void deleteStaff(int staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + staffId));
        //check disable
        if (staff.getStaffStatus() == StaffStatus.Disable) {
            throw new RuntimeException("Nhân viên (ID=" + staffId + ") đã bị vô hiệu hóa trước đó.");
        }
        // set disable
        staff.setStaffStatus(StaffStatus.Disable);
        staffRepository.save(staff);

        System.out.println("Đã xóa nhân viên: " + staff.getStaffName() + " (ID = " + staffId + ")");
    }


    // sắp xếp + phân trang
    public Page<Staff> getPaginatedAndSortedStaffs(int pageNo, int pageSize, String sortField, String sortDir) {

        int pageIndex = pageNo - 1;

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        // không lấy disable
        return staffRepository.findByStaffStatusNot(StaffStatus.Disable, pageable);
    }
}
