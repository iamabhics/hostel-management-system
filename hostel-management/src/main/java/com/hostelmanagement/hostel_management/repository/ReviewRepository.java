package com.hostelmanagement.hostel_management.repository;

import com.hostelmanagement.hostel_management.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByHostelIdOrderByCreatedAtDesc(Long hostelId);

    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);
}