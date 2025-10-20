package com.example.managebyclesystem.repository;

import com.example.managebyclesystem.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    List<Staff> findByStaffStatus(Staff.StaffStatus status);
}
