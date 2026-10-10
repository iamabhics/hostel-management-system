package com.hostelmanagement.hostel_management.repository;

import com.hostelmanagement.hostel_management.entity.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostelRepository extends JpaRepository<Hostel, Long> {

    List<Hostel> findByNameContainingIgnoreCaseOrAreaContainingIgnoreCaseOrCityContainingIgnoreCase(
            String name,
            String area,
            String city
    );
}