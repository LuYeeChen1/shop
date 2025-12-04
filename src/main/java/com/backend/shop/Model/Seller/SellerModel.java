package com.backend.shop.Model.Seller;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * SellerModel stores seller-specific profile and verification data.
 * It maps to the "sellers" table and references the users table via user_id.
 */
@Entity
@Table(name = "sellers")
public class SellerModel {

    // Primary Key + Foreign Key to users.id
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Email of the seller (same as the user's email)
    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "shop_name", length = 200)
    private String shopName;

    @Column(name = "shop_description", length = 500)
    private String shopDescription;

    @Column(name = "shop_logo_url", length = 500)
    private String shopLogoUrl;

    @Column(name = "business_registration_number", length = 100)
    private String businessRegistrationNumber;

    @Column(name = "business_address", length = 500)
    private String businessAddress;

    @Column(name = "contact_number", length = 50)
    private String contactNumber;

    // Seller status: PENDING / APPROVED / REJECTED
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private SellerStatus status;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "reviewed_by_admin", length = 200)
    private String reviewedByAdmin;

    @Column(name = "review_comment", length = 500)
    private String reviewComment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor for JPA
    public SellerModel() {
    }

    // Getters & Setters

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {this.shopName = shopName; }

    public String getShopDescription() {
        return shopDescription;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public String getShopLogoUrl() {
        return shopLogoUrl;
    }

    public void setShopLogoUrl(String shopLogoUrl) {
        this.shopLogoUrl = shopLogoUrl;
    }

    public String getBusinessRegistrationNumber() {
        return businessRegistrationNumber;
    }

    public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public SellerStatus getStatus() {
        return status;
    }

    public void setStatus(SellerStatus status) {
        this.status = status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getReviewedByAdmin() {
        return reviewedByAdmin;
    }

    public void setReviewedByAdmin(String reviewedByAdmin) {
        this.reviewedByAdmin = reviewedByAdmin;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
