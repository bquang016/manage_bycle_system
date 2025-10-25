package com.example.managebyclesystem.repository;

import com.example.managebyclesystem.constants.MaintenanceStatus;
import com.example.managebyclesystem.model.Maintenance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceRepo extends JpaRepository<Maintenance, Integer> {


    @Query("""
        SELECT m FROM Maintenance m
        WHERE m.maintenaceStatus = :status
        """)
    Page<Maintenance> findByStatus(@Param("status") MaintenanceStatus status, Pageable pageable);

    @Query("""
        SELECT m FROM Maintenance m
        ORDER BY m.maintenanceDate ASC
        """)
    Page<Maintenance> findAllByOrderByMaintenanceDateAsc (Pageable pageable);

    @Query("""
        SELECT m FROM Maintenance m
        ORDER BY m.maintenanceDate DESC
        """)
    Page<Maintenance> findAllByOrderByMaintenanceDateDesc(Pageable pageable);


    @Query("""
        SELECT m FROM Maintenance m
        ORDER BY m.maintenanceCost ASC
        """)
    Page<Maintenance> findAllByOrderByMaintenanceCostAsc(Pageable pageable);

    @Query("""
        SELECT m FROM Maintenance m
        ORDER BY m.maintenanceCost DESC
        """)
    Page<Maintenance> findAllByOrderByMaintenanceCostDesc(Pageable pageable);
}
