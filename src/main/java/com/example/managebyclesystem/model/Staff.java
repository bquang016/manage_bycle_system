package com.example.managebyclesystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "staff")
@Check(constraints = "staff_status IN ('Able','Disable') AND staff_position IN ('MANAGER','STAFF','SECURITY','MAINTENANCE') AND staff_shift IN ('MORNING','AFTERNOON','EVENING','FULLDAY')")
public class Staff {

    public enum StaffPosition { MANAGER, STAFF, SECURITY, MAINTENANCE }
    public enum StaffStatus { Able, Disable }
    public enum StaffShift { MORNING, AFTERNOON, EVENING, FULLDAY }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int staffId;

    @Column(nullable = false)
    private String staffName;

    @Enumerated(EnumType.STRING)
    @Column(name = "staff_position", nullable = false)
    private StaffPosition staffPosition;

    @Column(nullable = false)
    private double staffSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "staff_shift", nullable = false)
    private StaffShift staffShift;

    @Enumerated(EnumType.STRING)
    @Column(name = "staff_status", nullable = false)
    private StaffStatus staffStatus = StaffStatus.Able;

    // secu
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StaffRole role = StaffRole.ROLE_USER; // mặc định user


    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }

    public StaffPosition getStaffPosition() { return staffPosition; }
    public void setStaffPosition(StaffPosition staffPosition) { this.staffPosition = staffPosition; }

    public double getStaffSalary() { return staffSalary; }
    public void setStaffSalary(double staffSalary) { this.staffSalary = staffSalary; }

    public StaffShift getStaffShift() { return staffShift; }
    public void setStaffShift(StaffShift staffShift) { this.staffShift = staffShift; }

    public StaffStatus getStaffStatus() { return staffStatus; }
    public void setStaffStatus(StaffStatus staffStatus) { this.staffStatus = staffStatus; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public StaffRole getRole() { return role; }
    public void setRole(StaffRole role) { this.role = role; }
}
