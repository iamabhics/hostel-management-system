package com.hostelmanagement.hostel_management.repository;

import com.hostelmanagement.hostel_management.entity.ReviewRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRatingRepository extends JpaRepository<ReviewRating, Long> {

    Optional<ReviewRating> findByReviewId(Long reviewId);
}