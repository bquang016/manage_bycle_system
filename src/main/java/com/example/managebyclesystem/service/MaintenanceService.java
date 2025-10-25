package com.example.managebyclesystem.service;

import com.example.managebyclesystem.constants.MaintenanceStatus;
import com.example.managebyclesystem.model.Maintenance;
import com.example.managebyclesystem.repository.MaintenanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}