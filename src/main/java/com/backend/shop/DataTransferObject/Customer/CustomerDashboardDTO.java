package com.backend.shop.DataTransferObject.Customer;

/**
 * DTO for Customer Dashboard view.
 * Contains only data needed by the UI.
 */
public class CustomerDashboardDTO {

    private Long userId;
    private String username;
    private String email;

    // Seller related info for UI
    // NONE / PENDING / APPROVED / REJECTED
    private String sellerStatus;

    // Whether a seller record exists in the database
    private boolean sellerExists;

    // Admin review comment (mainly for REJECTED case)
    private String reviewComment;

    // Getters and setters

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

    public String getSellerStatus() {
        return sellerStatus;
    }

    public void setSellerStatus(String sellerStatus) {
        this.sellerStatus = sellerStatus;
    }

    public boolean isSellerExists() {
        return sellerExists;
    }

    public void setSellerExists(boolean sellerExists) {
        this.sellerExists = sellerExists;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}
