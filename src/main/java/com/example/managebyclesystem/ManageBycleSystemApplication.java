package com.example.managebyclesystem;

import com.example.managebyclesystem.model.Staff;
import com.example.managebyclesystem.service.StaffService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import java.util.List;

@SpringBootApplication
public class ManageBycleSystemApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ManageBycleSystemApplication.class, args);
        StaffService staffService = context.getBean(StaffService.class);

        System.out.println("===== DANH SÁCH NHÂN VIÊN (StaffStatus = Able) =====");
        List<Staff> activeStaffs = staffService.getAllActiveStaffs();
        activeStaffs.forEach(staff -> System.out.println(
                staff.getStaffId() + " - " +
                        staff.getStaffName() + " - " +
                        staff.getStaffPosition() + " - " +
                        staff.getStaffSalary() + " - " +
                        staff.getStaffShift()
        ));
    }
}
