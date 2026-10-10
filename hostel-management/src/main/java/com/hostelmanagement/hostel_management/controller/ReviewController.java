package com.hostelmanagement.hostel_management.controller;

import com.hostelmanagement.hostel_management.dto.ReviewDTO;
import com.hostelmanagement.hostel_management.service.ReviewService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // READ: Get all reviews for a particular hostel
    @GetMapping("/api/hostels/{hostelId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getHostelReviews(
            @PathVariable Long hostelId) {

        return ResponseEntity.ok(
                reviewService.getHostelReviews(hostelId)
        );
    }

    // CREATE: Add a review
    @PostMapping("/api/hostels/{hostelId}/reviews")
    public ResponseEntity<ReviewDTO> createReview(
            @PathVariable Long hostelId,
            @Valid @RequestBody ReviewDTO dto) {

        return ResponseEntity.ok(
                reviewService.createReview(hostelId, dto)
        );
    }

    // READ: Get reviews written by the logged-in student
    @GetMapping("/api/reviews/my")
    public ResponseEntity<List<ReviewDTO>> getMyReviews() {

        return ResponseEntity.ok(
                reviewService.getMyReviews()
        );
    }

    // UPDATE: Edit an existing review
    @PutMapping("/api/reviews/{id}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewDTO dto) {

        return ResponseEntity.ok(
                reviewService.updateReview(id, dto)
        );
    }

    // DELETE: Remove an existing review
    @DeleteMapping("/api/reviews/{id}")
    public ResponseEntity<Map<String, String>> deleteReview(
            @PathVariable Long id) {

        reviewService.deleteReview(id);

        return ResponseEntity.ok(
                Map.of("message", "Review deleted successfully")
        );
    }
}