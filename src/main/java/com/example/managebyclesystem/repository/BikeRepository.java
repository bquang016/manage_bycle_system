package com.example.managebyclesystem.repository;

import com.example.managebyclesystem.model.Bike;
import com.example.managebyclesystem.model.Bike.BikeType;
import com.example.managebyclesystem.model.Bike.BikeStatus;
import com.example.managebyclesystem.model.Bike.ActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BikeRepository extends JpaRepository<Bike, Integer> {
    @Query("""
    SELECT b FROM Bike b
    WHERE b.bikeActiveStatus = :status
    """)
    Page<Bike> findByActiveStatus(@Param("status") ActiveStatus status, Pageable pageable);

    @Query("""
    SELECT b FROM Bike b
    ORDER BY b.bikeName ASC
    """)
    Page<Bike> findAllByOrderByBikeNameAsc(Pageable pageable);

    @Query("""
    SELECT b FROM Bike b
    ORDER BY b.bikeName DESC
    """)
    Page<Bike> findAllByOrderByBikeNameDesc(Pageable pageable);

    @Query("""
    SELECT b FROM Bike b
    ORDER BY b.bikeRentPerHour ASC
    """)
    Page<Bike> findAllByOrderByBikeRentPerHourAsc(Pageable pageable);

    @Query("""
    SELECT b FROM Bike b
    ORDER BY b.bikeRentPerHour DESC
    """)
    Page<Bike> findAllByOrderByBikeRentPerHourDesc(Pageable pageable);

    @Query("""
    SELECT b FROM Bike b
    WHERE (:name IS NULL OR LOWER(b.bikeName) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:type IS NULL OR b.bikeType = :type)
      AND (:location IS NULL OR LOWER(b.bikeLocation) LIKE LOWER(CONCAT('%', :location, '%')))
      AND b.bikeActiveStatus = com.example.managebyclesystem.model.Bike.ActiveStatus.ABLE
    """)
    Page<Bike> searchBikes(
            @Param("name") String name,
            @Param("type") BikeType type,
            @Param("location") String location,
            Pageable pageable
    );

}