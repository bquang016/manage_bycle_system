package com.example.managebyclesystem;

import com.example.managebyclesystem.model.Staff;
import com.example.managebyclesystem.model.StaffRole;
import com.example.managebyclesystem.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (staffRepository.findByUsername("admin").isEmpty()) {
            System.out.println(">>> Creating ADMIN user...");

            Staff adminUser = new Staff();
            adminUser.setStaffName("Admin User");
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(StaffRole.ROLE_ADMIN);
            adminUser.setStaffStatus(Staff.StaffStatus.Able);

            adminUser.setStaffPosition(Staff.StaffPosition.MANAGER);
            adminUser.setStaffSalary(5000000);
            adminUser.setStaffShift(Staff.StaffShift.FULLDAY);

            staffRepository.save(adminUser);
            System.out.println(">>> ADMIN user created successfully!");
        } else {
            System.out.println(">>> ADMIN user already exists.");
        }

        if (staffRepository.findByUsername("user").isEmpty()) {
            System.out.println(">>> Creating USER user...");
            Staff normalUser = new Staff();
            normalUser.setStaffName("Normal User");
            normalUser.setUsername("user");
            normalUser.setPassword(passwordEncoder.encode("user123"));
            normalUser.setRole(StaffRole.ROLE_USER);
            normalUser.setStaffStatus(Staff.StaffStatus.Able);
            normalUser.setStaffPosition(Staff.StaffPosition.STAFF);
            normalUser.setStaffSalary(3000000); // Ví dụ
            normalUser.setStaffShift(Staff.StaffShift.MORNING);
            staffRepository.save(normalUser);
            System.out.println(">>> USER user created successfully!");
        } else {
            System.out.println(">>> USER user already exists.");
        }
    }
}