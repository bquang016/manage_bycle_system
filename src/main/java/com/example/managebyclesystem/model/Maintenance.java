package com.example.managebyclesystem.model;

import com.example.managebyclesystem.constants.MaintenanceStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Maintenance {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maintenanceId;

    @ManyToOne
    @JoinColumn(name = "bikeId")
    private Bike bikeId;
    private LocalDate maintenanceDate ;
    private String maintenanceDesc;
    private double maintenanceCost;
    @Enumerated(EnumType.STRING)
    private MaintenanceStatus maintenaceStatus = MaintenanceStatus.ABLE;

    public Maintenance(){

    }

    public int getMaintenanceId(){
        return maintenanceId;
    }
    public void setMaintenanceId(int id){
        this.maintenanceId = id;
    }
    public Bike getBikeId(){
        return bikeId;
    }
    public void setBikeId(Bike id){
        this.bikeId = id;
    }
    public LocalDate getMaintenanceDate(){
        return maintenanceDate;
    }
    public void setMaintenanceDate(LocalDate date){
        this.maintenanceDate = date;
    }
    public String getMaintenanceDesc(){
        return maintenanceDesc;
    }
    public void setMaintenanceDesc(String Desc){
        this.maintenanceDesc = Desc;
    }
    public double getMaintenanceCost(){
        return maintenanceCost;
    }
    public void setMaintenanceCost(double cost){
        this.maintenanceCost = cost;
    }
    public MaintenanceStatus getMaintenanceStatus(){
        return maintenaceStatus;
    }
    public void setMaintenanceStatus(MaintenanceStatus status){
        this.maintenaceStatus = status;
    }

}