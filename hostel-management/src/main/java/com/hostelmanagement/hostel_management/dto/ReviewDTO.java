package com.hostelmanagement.hostel_management.dto;

import jakarta.validation.constraints.Size;

public class ReviewDTO {

    private Long id;
    private Long userId;
    private Long hostelId;

   @Size(max = 100, message = "Review title cannot exceed 100 characters")
    private String title;

    @Size(max = 1000, message = "Review description cannot exceed 1000 characters")
    private String description;

    private Double overallRating;

    private Double food;
    private Double cleanliness;
    private Double room;
    private Double facilities;
    private Double safety;
    private Double staffBehaviour;
    private Double valueForMoney;
    private Double maintenance;

    private String createdAt;
    private String updatedAt;

    public ReviewDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHostelId() {
        return hostelId;
    }

    public void setHostelId(Long hostelId) {
        this.hostelId = hostelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(Double overallRating) {
        this.overallRating = overallRating;
    }

    public Double getFood() {
        return food;
    }

    public void setFood(Double food) {
        this.food = food;
    }

    public Double getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(Double cleanliness) {
        this.cleanliness = cleanliness;
    }

    public Double getRoom() {
        return room;
    }

    public void setRoom(Double room) {
        this.room = room;
    }

    public Double getFacilities() {
        return facilities;
    }

    public void setFacilities(Double facilities) {
        this.facilities = facilities;
    }

    public Double getSafety() {
        return safety;
    }

    public void setSafety(Double safety) {
        this.safety = safety;
    }

    public Double getStaffBehaviour() {
        return staffBehaviour;
    }

    public void setStaffBehaviour(Double staffBehaviour) {
        this.staffBehaviour = staffBehaviour;
    }

    public Double getValueForMoney() {
        return valueForMoney;
    }

    public void setValueForMoney(Double valueForMoney) {
        this.valueForMoney = valueForMoney;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Double getMaintenance() {
    return maintenance;
    }

    public void setMaintenance(Double maintenance) {
        this.maintenance = maintenance;
    }
}