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

    public void addBike(Bike bike) {
        if (bike.getBikeName() == null || bike.getBikeName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên xe đạp không được để trống");
        }
        if (bike.getBikeRentPerHour() < 0) {
            throw new IllegalArgumentException("Giá thuê mỗi giờ phải lớn hơn hoặc bằng 0");
        }
        if (bike.getBikeType() == null) {
            throw new IllegalArgumentException("Loại xe không hợp lệ");
        }
        if (bike.getBikeStatus() == null) {
            throw new IllegalArgumentException("Trạng thái xe không hợp lệ");
        }

        bike.setBikeActiveStatus(Bike.ActiveStatus.ABLE);

        bikeRepository.save(bike);
        System.out.println("Thêm xe đạp thành công: " + bike.getBikeName());
    }

    public List<Bike> getAllBikes() {
        return bikeRepository.findAll().stream()
                .filter(bike -> bike.getBikeActiveStatus() == Bike.ActiveStatus.ABLE)
                .toList();
    }

    public void updateBike(Bike updatedBike) {
        if (updatedBike.getBikeId() == 0) {
            throw new IllegalArgumentException("ID xe đạp không hợp lệ để cập nhật");
        }

        Bike existingBike = bikeRepository.findById(updatedBike.getBikeId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy xe đạp có ID: " + updatedBike.getBikeId()));

        if (updatedBike.getBikeName() == null || updatedBike.getBikeName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên xe đạp không được để trống");
        }
        if (updatedBike.getBikeRentPerHour() < 0) {
            throw new IllegalArgumentException("Giá thuê mỗi giờ phải ≥ 0");
        }

        existingBike.setBikeName(updatedBike.getBikeName().trim());
        existingBike.setBikeType(updatedBike.getBikeType());
        existingBike.setBikeRentPerHour(updatedBike.getBikeRentPerHour());
        existingBike.setBikeLocation(updatedBike.getBikeLocation());
        existingBike.setBikeStatus(updatedBike.getBikeStatus());

        if (updatedBike.getBikeImage() != null && !updatedBike.getBikeImage().isEmpty()) {
            existingBike.setBikeImage(updatedBike.getBikeImage());
        }
        bikeRepository.save(existingBike);
        System.out.println("Cập nhật xe đạp thành công: " + existingBike.getBikeName());
    }

    public void disableBike(int id) {
        Bike bike = bikeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy xe đạp có ID: " + id));

        if (bike.getBikeActiveStatus() == Bike.ActiveStatus.DISABLE) {
            throw new IllegalStateException("Xe đạp này đã bị vô hiệu hóa trước đó.");
        }

        bike.setBikeActiveStatus(Bike.ActiveStatus.DISABLE);
        bikeRepository.save(bike);

        System.out.println("Xe đạp ID " + id + " đã được chuyển sang trạng thái DISABLE");
    }
}