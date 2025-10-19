package com.example.managebyclesystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "staff")
@Check(constraints = "staff_status IN ('Able','Disable') AND staff_position IN ('MANAGER','STAFF','SECURITY','MAINTENANCE') AND staff_shift IN ('MORNING','AFTERNOON','EVENING','FULLDAY')")
public class Staff {
    public enum StaffPosition {
        MANAGER,
        STAFF,
        SECURITY,
        MAINTENANCE
    }

    public enum StaffStatus {
        Able,
        Disable
    }

    public enum StaffShift {
        MORNING,
        AFTERNOON,
        EVENING,
        FULLDAY
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int staffId;

    private String staffName;

    @Enumerated(EnumType.STRING)
    @Column(name = "staff_position", columnDefinition = "ENUM('MANAGER','STAFF','SECURITY','MAINTENANCE')")
    private StaffPosition staffPosition;

    private double staffSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "staff_shift", columnDefinition = "ENUM('MORNING','AFTERNOON','EVENING','FULLDAY')")
    private StaffShift staffShift;

    private boolean staffRoles;

    @Enumerated(EnumType.STRING)
    @Column(name = "staff_status", columnDefinition = "ENUM('Able','Disable')")
    private StaffStatus staffStatus;

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public StaffPosition getStaffPosition() {
        return staffPosition;
    }

    public void setStaffPosition(StaffPosition staffPosition) {
        this.staffPosition = staffPosition;
    }

    public double getStaffSalary() {
        return staffSalary;
    }

    public void setStaffSalary(double staffSalary) {
        this.staffSalary = staffSalary;
    }

    public StaffShift getStaffShift() {
        return staffShift;
    }

    public void setStaffShift(StaffShift staffShift) {
        this.staffShift = staffShift;
    }

    public boolean isStaffRoles() {
        return staffRoles;
    }

    public void setStaffRoles(boolean staffRoles) {
        this.staffRoles = staffRoles;
    }

    public StaffStatus getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(StaffStatus staffStatus) {
        this.staffStatus = staffStatus;
    }
}
