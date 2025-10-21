package com.example.managebyclesystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "bike")
@Check(constraints = "bike_status IN ('Available','Unavailable','Maintenance') AND bike_type IN ('CITY','MOUNTAIN','ELECTRIC','KID')")
public class Bike {

    public void setBikeRentPerHour(double rentPerHour) {
    }

    public enum BikeType {
        NORMAL,
        ELECTRIC,
        KID
    }

    public enum BikeStatus {
        Available,
        Unavailable,
        Maintenance
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bikeId;

    private String bikeImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "bike_type", columnDefinition = "ENUM('NORMAL','ELECTRIC','KID')")
    private BikeType bikeType;

    private String bikeName;

    @Column(name = "hourly_rate")
    private double hourlyRate;

    private String bikeLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "bike_status", columnDefinition = "ENUM('Available','Unavailable','Maintenance')")
    private BikeStatus bikeStatus;

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    public String getBikeImage() {
        return bikeImage;
    }

    public void setBikeImage(String bikeImage) {
        this.bikeImage = bikeImage;
    }

    public BikeType getBikeType() {
        return bikeType;
    }

    public void setBikeType(BikeType bikeType) {
        this.bikeType = bikeType;
    }

    public String getBikeName() {
        return bikeName;
    }

    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getBikeLocation() {
        return bikeLocation;
    }

    public void setBikeLocation(String bikeLocation) {
        this.bikeLocation = bikeLocation;
    }

    public BikeStatus getBikeStatus() {
        return bikeStatus;
    }

    public void setBikeStatus(BikeStatus bikeStatus) {
        this.bikeStatus = bikeStatus;
    }
}
