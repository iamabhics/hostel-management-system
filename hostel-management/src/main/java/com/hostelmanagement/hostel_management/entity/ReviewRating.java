
package com.hostelmanagement.hostel_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "review_ratings")
public class ReviewRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reviewId;

    // Optional category ratings, each from 0 to 10
    private Double food;
    private Double cleanliness;
    private Double room;
    private Double facilities;
    private Double safety;
    private Double staffBehaviour;
    private Double valueForMoney;
    private Double maintenance;

    public ReviewRating() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
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

    public Double getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Double maintenance) {
        this.maintenance = maintenance;
    }
}