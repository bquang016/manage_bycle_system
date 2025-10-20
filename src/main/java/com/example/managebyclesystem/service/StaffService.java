package com.example.managebyclesystem.service;

import com.example.managebyclesystem.model.Staff;
import com.example.managebyclesystem.model.Staff.StaffPosition;
import com.example.managebyclesystem.model.Staff.StaffShift;
import com.example.managebyclesystem.model.Staff.StaffStatus;
import com.example.managebyclesystem.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class StaffService {

    private final StaffRepository staffRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public void addStaff(String name, String position, double salary, String shift, boolean roles) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        }

        if (salary <= 0) {
            throw new IllegalArgumentException("Lương phải lớn hơn 0");
        }


        StaffPosition staffPosition;
        try {
            staffPosition = StaffPosition.valueOf(position.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Chức vụ không hợp lệ. Chỉ được: MANAGER, STAFF, SECURITY, MAINTENANCE");
        }


        StaffShift staffShift;
        try {
            staffShift = StaffShift.valueOf(shift.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ca làm việc không hợp lệ. Chỉ được: MORNING, AFTERNOON, EVENING, FULLDAY");
        }

        // Tạo đối tượng nv mới
        Staff staff = new Staff();
        staff.setStaffName(name.trim());
        staff.setStaffPosition(staffPosition);
        staff.setStaffSalary(salary);
        staff.setStaffShift(staffShift);
        staff.setStaffRoles(roles);
        staff.setStaffStatus(StaffStatus.Able); // Mặc định able


        staffRepository.save(staff);

        System.out.println("Thêm nhân viên thành công: " + name);
    }

    // Lấy nv able
    public List<Staff> getAllActiveStaffs() {
        return staffRepository.findByStaffStatus(Staff.StaffStatus.Able);
    }
}
