package com.example.managebyclesystem.service;

import com.example.managebyclesystem.model.Bike;
import com.example.managebyclesystem.model.Bike.BikeType;
import com.example.managebyclesystem.model.Bike.BikeStatus;
import com.example.managebyclesystem.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeService {

    private final BikeRepository bikeRepository;

    @Autowired
    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    public void addBike(String bikeName, String type, String color, double rentPerHour, String status, String note) {
        if (bikeName == null || bikeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên xe đạp không được để trống");
        }

        if (rentPerHour < 0) {
            throw new IllegalArgumentException("Giá thuê mỗi giờ phải lớn hơn hoặc bằng 0");
        }

        BikeType bikeType;
        try {
            bikeType = BikeType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Loại xe không hợp lệ. Chỉ được: NORMAL, MOUNTAIN, ELECTRIC, KID");
        }

        BikeStatus bikeStatus;
        try {
            bikeStatus = BikeStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái xe không hợp lệ. Chỉ được: AVAILABLE, UNAVAILABLE, MAINTENANCE");
        }

        Bike bike = new Bike();
        bike.setBikeName(bikeName.trim());
        bike.setBikeType(bikeType);
        bike.setBikeRentPerHour(rentPerHour);
        bike.setBikeStatus(bikeStatus);

        bikeRepository.save(bike);
        System.out.println("Thêm xe đạp thành công: " + bikeName);
    }
    public List<Bike> getAllBikes() {
        return bikeRepository.findAll();
    }

    public void updateBike(int id, String name, String type, double hourlyRate, String location, String status) {
        var bikeOpt = bikeRepository.findById(id);
        if (bikeOpt.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy xe đạp có ID: " + id);
        }

        Bike bike = bikeOpt.get();

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên xe đạp không được để trống");
        }

        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Giá thuê mỗi giờ phải ≥ 0");
        }
        Bike.BikeType bikeType;
        try {
            bikeType = Bike.BikeType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Loại xe không hợp lệ. Chỉ được: NORMAL, ELECTRIC, KID");
        }
        Bike.BikeStatus bikeStatus;
        try {
            bikeStatus = Bike.BikeStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ. Chỉ được: Available, Unavailable, Maintenance");
        }

        bike.setBikeName(name.trim());
        bike.setBikeType(bikeType);
        bike.setBikeRentPerHour(hourlyRate);
        bike.setBikeLocation(location);
        bike.setBikeStatus(bikeStatus);

        bikeRepository.save(bike);
        System.out.println("Cập nhật xe đạp thành công: " + bike.getBikeName());
    }

}
