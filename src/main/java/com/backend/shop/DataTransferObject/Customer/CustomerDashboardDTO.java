package com.backend.shop.DataTransferObject.Customer;

/**
 * DTO for the customer dashboard view.
 * Controls what the UI should display based on seller status.
 */
public class CustomerDashboardDTO {

    private Long userId;
    private String username;
    private String email;

    // Whether a seller record exists in the "sellers" table
    private boolean sellerExists;

    /**
     * Seller status for UI:
     *  - "NONE"     : no seller record yet
     *  - "PENDING"  : waiting for admin review
     *  - "APPROVED" : can enter seller dashboard
     *  - "REJECTED" : rejected by admin
     */
    private String sellerStatus;

    // Optional comment from admin if status is REJECTED
    private String reviewComment;

    // ==========================
    // Getters and Setters
    // ==========================

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSellerExists() {
        return sellerExists;
    }

    public void setSellerExists(boolean sellerExists) {
        this.sellerExists = sellerExists;
    }

    public String getSellerStatus() {
        return sellerStatus;
    }

    public void setSellerStatus(String sellerStatus) {
        this.sellerStatus = sellerStatus;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}
