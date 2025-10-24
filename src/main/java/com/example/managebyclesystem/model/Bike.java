package com.example.managebyclesystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "bike")
@Check(constraints = "bike_status IN ('Available','Unavailable','Maintenance') AND bike_type IN ('NORMAL','ELECTRIC','KID')")
public class Bike {

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
    public enum ActiveStatus {
        ABLE,
        DISABLE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bikeId;

    // Ảnh xe — chỉ giữ 1 field
    @Column(name = "bike_image")
    private String bikeImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "bike_type", columnDefinition = "ENUM('NORMAL','ELECTRIC','KID')")
    private BikeType bikeType;

    private String bikeName;

    @Column(name = "bike_rent_per_hour")
    private double bikeRentPerHour;

    private String bikeLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "bike_status", columnDefinition = "ENUM('Available','Unavailable','Maintenance')")
    private BikeStatus bikeStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "bike_active_status", columnDefinition = "ENUM('ABLE','DISABLE')")
    private ActiveStatus bikeActiveStatus;

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

    public double getBikeRentPerHour() {
        return bikeRentPerHour;
    }

    public void setBikeRentPerHour(double bikeRentPerHour) {
        this.bikeRentPerHour = bikeRentPerHour;
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

    public ActiveStatus getBikeActiveStatus() {
        return bikeActiveStatus;
    }

    public void setBikeActiveStatus(ActiveStatus bikeActiveStatus) {
        this.bikeActiveStatus = bikeActiveStatus;
    }
}
