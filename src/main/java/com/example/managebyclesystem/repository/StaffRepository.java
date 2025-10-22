package com.example.managebyclesystem.repository;

import com.example.managebyclesystem.model.Staff;
import com.example.managebyclesystem.model.Staff.StaffPosition;
import com.example.managebyclesystem.model.Staff.StaffStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    List<Staff> findByStaffStatus(Staff.StaffStatus status);

    @Query("SELECT s FROM Staff s " +
            "WHERE s.staffStatus = 'Able' " +
            "AND (LOWER(s.staffName) LIKE LOWER(:keyword) " +
            "OR s.staffPosition = :position)")
    List<Staff> searchStaffs(@Param("keyword") String keyword, @Param("position") StaffPosition position);
    Page<Staff> findByStaffStatusNot(StaffStatus status, Pageable pageable);
}
