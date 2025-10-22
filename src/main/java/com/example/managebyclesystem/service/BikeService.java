package com.example.managebyclesystem.service;

import com.example.managebyclesystem.model.Bike;
import com.example.managebyclesystem.model.Bike.BikeType;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import com.example.managebyclesystem.repository.BikeRepository;
import org.springframework.data.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;

import java.util.List;

@Service
public class BikeService {

    private final BikeRepository bikeRepository;
    private static final String UPLOAD_DIR = "uploads/bikes/";
    @Autowired
    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    public void addBike(Bike bike, MultipartFile imageFile) {
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
        // ✅ Lưu ảnh nếu có tải lên
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.createDirectories(path.getParent());
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                bike.setBikeImage("/uploads/bikes/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi lưu ảnh: " + e.getMessage());
            }
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

    public void updateBike(Bike updatedBike, MultipartFile imageFile) {
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

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.createDirectories(path.getParent());
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                existingBike.setBikeImage("/uploads/bikes/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi cập nhật ảnh: " + e.getMessage());
            }
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

    public List<Bike> searchBikes(String name, BikeType type, String location) {
        return bikeRepository.findAll().stream()
                .filter(bike ->
                        bike.getBikeActiveStatus() == Bike.ActiveStatus.ABLE &&
                                (name == null || name.isEmpty() || bike.getBikeName().toLowerCase().contains(name.toLowerCase())) &&
                                (type == null || bike.getBikeType() == type) &&
                                (location == null || location.isEmpty() || bike.getBikeLocation().toLowerCase().contains(location.toLowerCase()))
                )
                .toList();
    }

    public Page<Bike> getPagedAndSortedBikes(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // Lấy tất cả xe rồi lọc ABLE
        List<Bike> ableBikes = bikeRepository.findAll(sort).stream()
                .filter(bike -> bike.getBikeActiveStatus() == Bike.ActiveStatus.ABLE)
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), ableBikes.size());

        List<Bike> pagedBikes = ableBikes.subList(start, end);

        return new PageImpl<>(pagedBikes, pageable, ableBikes.size());
    }
}