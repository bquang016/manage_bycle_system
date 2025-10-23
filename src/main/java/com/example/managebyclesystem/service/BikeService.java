package com.example.managebyclesystem.service;

import com.example.managebyclesystem.model.Bike;
import com.example.managebyclesystem.model.Bike.BikeType;
import com.example.managebyclesystem.model.Bike.BikeStatus;
import com.example.managebyclesystem.model.Bike.ActiveStatus;
import com.example.managebyclesystem.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Service
public class BikeService {
    private final BikeRepository bikeRepository;
    private static final String UPLOAD_DIR = "uploads/bikes/";

    @Autowired
    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    public void addBike(Bike bike, MultipartFile imageFile) {
        validateBike(bike);

        if (imageFile != null && !imageFile.isEmpty()) {
            bike.setBikeImage(saveImage(imageFile));
        }

        bike.setBikeActiveStatus(ActiveStatus.ABLE);
        bikeRepository.save(bike);
    }

    public void updateBike(Bike updatedBike, MultipartFile imageFile) {
        if (updatedBike.getBikeId() == 0) {
            throw new IllegalArgumentException("ID xe đạp không hợp lệ để cập nhật");
        }

        Bike existingBike = bikeRepository.findById(updatedBike.getBikeId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy xe đạp có ID: " + updatedBike.getBikeId()));

        validateBike(updatedBike);

        existingBike.setBikeName(updatedBike.getBikeName().trim());
        existingBike.setBikeType(updatedBike.getBikeType());
        existingBike.setBikeRentPerHour(updatedBike.getBikeRentPerHour());
        existingBike.setBikeLocation(updatedBike.getBikeLocation());
        existingBike.setBikeStatus(updatedBike.getBikeStatus());

        if (imageFile != null && !imageFile.isEmpty()) {
            existingBike.setBikeImage(saveImage(imageFile));
        }

        bikeRepository.save(existingBike);
    }

    public void disableBike(int id) {
        Bike bike = bikeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy xe đạp có ID: " + id));

        if (bike.getBikeActiveStatus() == ActiveStatus.DISABLE) {
            throw new IllegalStateException("Xe đạp này đã bị vô hiệu hóa trước đó.");
        }

        bike.setBikeActiveStatus(ActiveStatus.DISABLE);
        bikeRepository.save(bike);
    }

    public Page<Bike> getAllBikes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bikeRepository.findByActiveStatus(ActiveStatus.ABLE, pageable);
    }

    public Page<Bike> getAllByOrderByNameAsc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bikeRepository.findAllByOrderByBikeNameAsc(pageable);
    }

    public Page<Bike> getAllByOrderByNameDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bikeRepository.findAllByOrderByBikeNameDesc(pageable);
    }

    public Page<Bike> getAllByOrderByPriceAsc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bikeRepository.findAllByOrderByBikeRentPerHourAsc(pageable);
    }

    public Page<Bike> getAllByOrderByPriceDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bikeRepository.findAllByOrderByBikeRentPerHourDesc(pageable);
    }

    public Page<Bike> searchBikes(String name, BikeType type, String location, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bikeRepository.searchBikes(name, type, location, pageable);
    }

    public Optional<Bike> getBikeById(int id) {
        return bikeRepository.findById(id);
    }

    // ---------------- Helper Methods ----------------
    private void validateBike(Bike bike) {
        if (bike.getBikeName() == null || bike.getBikeName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên xe đạp không được để trống");
        }
        if (bike.getBikeRentPerHour() < 0) {
            throw new IllegalArgumentException("Giá thuê mỗi giờ phải ≥ 0");
        }
        if (bike.getBikeType() == null) {
            throw new IllegalArgumentException("Loại xe không hợp lệ");
        }
        if (bike.getBikeStatus() == null) {
            throw new IllegalArgumentException("Trạng thái xe không hợp lệ");
        }
    }

    private String saveImage(MultipartFile imageFile) {
        try {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/bikes/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu ảnh: " + e.getMessage());
        }
    }

}