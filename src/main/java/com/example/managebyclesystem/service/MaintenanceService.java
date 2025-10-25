package com.example.managebyclesystem.service;

import com.example.managebyclesystem.constants.MaintenanceStatus;
import com.example.managebyclesystem.model.Maintenance;
import com.example.managebyclesystem.repository.MaintenanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
public class MaintenanceService {

    private final MaintenanceRepo maintenanceRepo;

    private static final int PAGE_SIZE = 10;


    @Autowired
    public MaintenanceService(MaintenanceRepo maintenanceRepo) {
        this.maintenanceRepo = maintenanceRepo;
    }

    public Page<Maintenance> getAllMaintenance(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return maintenanceRepo.findByStatus(MaintenanceStatus.ABLE, pageable);
    }

    public Page<Maintenance> getAllByOrderByMaintenanceCostAsc(int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return maintenanceRepo.findAllByOrderByMaintenanceCostAsc(pageable);
    }

    public Page<Maintenance> getAllByOrderByMaintenanceCostDesc(int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return  maintenanceRepo.findAllByOrderByMaintenanceCostDesc(pageable);
    }

    public Page<Maintenance> getAllByOrderByMaitenanceDateAsc(int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return  maintenanceRepo.findAllByOrderByMaintenanceDateAsc(pageable);
    }

    public Page<Maintenance> getAllByOrderByMaintenanceDateDesc(int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return  maintenanceRepo.findAllByOrderByMaintenanceDateDesc(pageable);
    }
    public Maintenance addMaintenance(Maintenance maintenance) {
        if (maintenance.getBikeId() == null) {
            throw new IllegalArgumentException("Xe cần bảo trì không được để trống");
        }

        if (maintenance.getMaintenanceDesc() == null || maintenance.getMaintenanceDesc().trim().isEmpty()) {
            throw new IllegalArgumentException("Mô tả bảo trì không được để trống");
        }

        if (maintenance.getMaintenanceDate() == null) {
            throw new IllegalArgumentException("Ngày bảo trì không được để trống");
        }

        if (maintenance.getMaintenanceDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày bảo trì không được nằm trong tương lai");
        }

        if (maintenance.getMaintenanceCost() < 0) {
            throw new IllegalArgumentException("Chi phí bảo trì không được nhỏ hơn 0");
        }

        return maintenanceRepo.save(maintenance);
    }

    public Optional<Maintenance> getMaintenanceById(int id){
        return maintenanceRepo.findById(id);
    }

    public Maintenance updateMaintenance(int id, Maintenance newData) {
        Maintenance existing = maintenanceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bảo trì có ID = " + id));

        boolean changed = false;

        if (newData.getBikeId() != null
                && (existing.getBikeId() == null ||
                existing.getBikeId().getBikeId() != newData.getBikeId().getBikeId())) {
            existing.setBikeId(newData.getBikeId());
            changed = true;
        }

        if (newData.getMaintenanceDesc() != null
                && !Objects.equals(existing.getMaintenanceDesc(), newData.getMaintenanceDesc())) {
            existing.setMaintenanceDesc(newData.getMaintenanceDesc());
            changed = true;
        }

        if (newData.getMaintenanceDate() != null
                && !Objects.equals(existing.getMaintenanceDate(), newData.getMaintenanceDate())) {

            if (newData.getMaintenanceDate().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Ngày bảo trì không được nằm trong tương lai.");
            }

            existing.setMaintenanceDate(newData.getMaintenanceDate());
            changed = true;
        }

        if (newData.getMaintenanceCost() >= 0
                && existing.getMaintenanceCost() != newData.getMaintenanceCost()) {
            existing.setMaintenanceCost(newData.getMaintenanceCost());
            changed = true;
        } else if (newData.getMaintenanceCost() < 0) {
            throw new IllegalArgumentException("Chi phí bảo trì không được nhỏ hơn 0.");
        }

        if (newData.getMaintenanceStatus() != null
                && !Objects.equals(existing.getMaintenanceStatus(), newData.getMaintenanceStatus())) {
            existing.setMaintenanceStatus(newData.getMaintenanceStatus());
            changed = true;
        }

        return changed ? maintenanceRepo.save(existing) : existing;
    }
}