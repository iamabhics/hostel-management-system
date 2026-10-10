
package com.hostelmanagement.hostel_management.service;

import com.hostelmanagement.hostel_management.dto.ReviewDTO;
import com.hostelmanagement.hostel_management.entity.Review;
import com.hostelmanagement.hostel_management.entity.ReviewRating;
import com.hostelmanagement.hostel_management.entity.User;
import com.hostelmanagement.hostel_management.enums.Role;
import com.hostelmanagement.hostel_management.repository.ReviewRatingRepository;
import com.hostelmanagement.hostel_management.repository.ReviewRepository;
import com.hostelmanagement.hostel_management.repository.UserRepository;
import com.hostelmanagement.hostel_management.repository.HostelRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewRatingRepository reviewRatingRepository;
    private final UserRepository userRepository;
    private final HostelRepository hostelRepository;

    public ReviewService(
        ReviewRepository reviewRepository,
        ReviewRatingRepository reviewRatingRepository,
        UserRepository userRepository,
        HostelRepository hostelRepository) {

    this.reviewRepository = reviewRepository;
    this.reviewRatingRepository = reviewRatingRepository;
    this.userRepository = userRepository;
    this.hostelRepository = hostelRepository;
    }

    private User getCurrentUser() {

        var authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || "anonymousUser".equals(authentication.getName())) {
            throw new RuntimeException("Please log in to continue");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    // Null means the student skipped this rating.
    // Any supplied rating must be between 0 and 10.
    private void validateRating(Double rating) {

        if (rating != null
                && (!Double.isFinite(rating)
                || rating < 0
                || rating > 10)) {
            throw new RuntimeException(
                    "Each rating must be between 0 and 10");
        }
    }

    private void validateRatings(ReviewDTO dto) {

        validateRating(dto.getOverallRating());
        validateRating(dto.getFood());
        validateRating(dto.getCleanliness());
        validateRating(dto.getRoom());
        validateRating(dto.getFacilities());
        validateRating(dto.getSafety());
        validateRating(dto.getStaffBehaviour());
        validateRating(dto.getValueForMoney());
        validateRating(dto.getMaintenance());
    }

    // Calculate the average using only categories actually rated.
    private Double calculateOverallRating(ReviewDTO dto) {

        List<Double> ratings = new ArrayList<>();

        if (dto.getFood() != null) {
            ratings.add(dto.getFood());
        }
        if (dto.getCleanliness() != null) {
            ratings.add(dto.getCleanliness());
        }
        if (dto.getRoom() != null) {
            ratings.add(dto.getRoom());
        }
        if (dto.getFacilities() != null) {
            ratings.add(dto.getFacilities());
        }
        if (dto.getSafety() != null) {
            ratings.add(dto.getSafety());
        }
        if (dto.getStaffBehaviour() != null) {
            ratings.add(dto.getStaffBehaviour());
        }
        if (dto.getValueForMoney() != null) {
            ratings.add(dto.getValueForMoney());
        }
        if (dto.getMaintenance() != null) {
            ratings.add(dto.getMaintenance());
        }

        if (ratings.isEmpty()) {
            return null;
        }

        double average = ratings.stream()
                .filter(rating -> rating != null)
                .mapToDouble(rating -> rating.doubleValue())
                .average()
                .orElseThrow();

        return Math.round(average * 100.0) / 100.0;
    }

    // Keep a manually supplied overall rating.
    // Calculate it only when the student leaves it blank.
    private Double resolveOverallRating(ReviewDTO dto) {

        if (dto.getOverallRating() != null) {
            return dto.getOverallRating();
        }

        return calculateOverallRating(dto);
    }

    private void validateReviewContent(ReviewDTO dto) {

        boolean hasTitle = dto.getTitle() != null
                && !dto.getTitle().isBlank();

        boolean hasDescription = dto.getDescription() != null
                && !dto.getDescription().isBlank();

        if (!hasTitle && !hasDescription) {
            throw new RuntimeException(
                    "Please provide a review title or description");
        }
    }

    @Transactional
    public ReviewDTO createReview(Long hostelId, ReviewDTO dto) {

        User user = getCurrentUser();

        if (user.getRole() != Role.VERIFIED_OLD_STUDENT) {
            throw new RuntimeException(
                    "Only verified old students can write reviews");
        }
        if (hostelId == null || !hostelRepository.existsById(hostelId)) {
      throw new RuntimeException("Hostel not found");
        }

        validateReviewContent(dto);
        validateRatings(dto);

        Review review = new Review();

        review.setUserId(user.getId());
        review.setHostelId(hostelId);
        review.setTitle(dto.getTitle());
        review.setDescription(dto.getDescription());
        review.setOverallRating(resolveOverallRating(dto));

        LocalDateTime now = LocalDateTime.now();
        review.setCreatedAt(now);
        review.setUpdatedAt(now);

        Review savedReview = reviewRepository.save(review);

        ReviewRating rating = new ReviewRating();
        rating.setReviewId(savedReview.getId());
        applyRatings(rating, dto);

        ReviewRating savedRating =
                reviewRatingRepository.save(rating);

        return convertToDTO(savedReview, savedRating);
    }

    public List<ReviewDTO> getHostelReviews(Long hostelId) {

        return reviewRepository
                .findByHostelIdOrderByCreatedAtDesc(hostelId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ReviewDTO> getMyReviews() {

        User user = getCurrentUser();

        return reviewRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewDTO dto) {

        User user = getCurrentUser();

        if (user.getRole() != Role.VERIFIED_OLD_STUDENT) {
            throw new RuntimeException(
                    "Only verified old students can edit reviews");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        if (!review.getUserId().equals(user.getId())) {
            throw new RuntimeException(
                    "You can only edit your own review");
        }

        validateReviewContent(dto);
        validateRatings(dto);

        review.setTitle(dto.getTitle());
        review.setDescription(dto.getDescription());
        review.setOverallRating(resolveOverallRating(dto));
        review.setUpdatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        ReviewRating rating = reviewRatingRepository
                .findByReviewId(reviewId)
                .orElseGet(ReviewRating::new);

        rating.setReviewId(reviewId);
        applyRatings(rating, dto);

        ReviewRating savedRating =
                reviewRatingRepository.save(rating);

        return convertToDTO(savedReview, savedRating);
    }

    @Transactional
    public void deleteReview(Long reviewId) {

        User user = getCurrentUser();

        if (user.getRole() != Role.VERIFIED_OLD_STUDENT) {
            throw new RuntimeException(
                    "Only verified old students can delete reviews");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        if (!review.getUserId().equals(user.getId())) {
            throw new RuntimeException(
                    "You can only delete your own review");
        }

        reviewRatingRepository
                .findByReviewId(reviewId)
                .ifPresent(reviewRatingRepository::delete);

        reviewRepository.delete(review);
    }

    private void applyRatings(
            ReviewRating rating,
            ReviewDTO dto) {

        rating.setFood(dto.getFood());
        rating.setCleanliness(dto.getCleanliness());
        rating.setRoom(dto.getRoom());
        rating.setFacilities(dto.getFacilities());
        rating.setSafety(dto.getSafety());
        rating.setStaffBehaviour(dto.getStaffBehaviour());
        rating.setValueForMoney(dto.getValueForMoney());
        rating.setMaintenance(dto.getMaintenance());
    }

    private ReviewDTO convertToDTO(Review review) {

        ReviewRating rating = reviewRatingRepository
                .findByReviewId(review.getId())
                .orElse(null);

        return convertToDTO(review, rating);
    }

    private ReviewDTO convertToDTO(
            Review review,
            ReviewRating rating) {

        ReviewDTO dto = new ReviewDTO();

        dto.setId(review.getId());
        dto.setUserId(review.getUserId());
        dto.setHostelId(review.getHostelId());
        dto.setTitle(review.getTitle());
        dto.setDescription(review.getDescription());
        dto.setOverallRating(review.getOverallRating());

        if (rating != null) {
            dto.setFood(rating.getFood());
            dto.setCleanliness(rating.getCleanliness());
            dto.setRoom(rating.getRoom());
            dto.setFacilities(rating.getFacilities());
            dto.setSafety(rating.getSafety());
            dto.setStaffBehaviour(rating.getStaffBehaviour());
            dto.setValueForMoney(rating.getValueForMoney());
            dto.setMaintenance(rating.getMaintenance());
        }

        if (review.getCreatedAt() != null) {
            dto.setCreatedAt(review.getCreatedAt().toString());
        }

        if (review.getUpdatedAt() != null) {
            dto.setUpdatedAt(review.getUpdatedAt().toString());
        }

        return dto;
    }
}