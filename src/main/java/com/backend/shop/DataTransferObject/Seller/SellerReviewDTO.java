package com.backend.shop.DataTransferObject.Seller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for admin reviewing a seller application.
 * Admin can approve or reject and optionally add a review comment.
 */
public class SellerReviewDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    private String email;

    private String shopName;

    private String currentStatus;

    /**
     * Decision made by admin:
     *  - "APPROVE"
     *  - "REJECT"
     */
    @NotBlank(message = "Decision is required")
    private String decision;

    /**
     * Optional comment from admin.
     * Recommended when rejecting.
     */
    private String reviewComment;

    // ===== Getters and Setters =====

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}
