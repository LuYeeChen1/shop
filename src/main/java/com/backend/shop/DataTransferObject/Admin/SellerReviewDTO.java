package com.backend.shop.DataTransferObject.Admin;

import com.backend.shop.Model.Seller.SellerStatus;

/**
 * SellerReviewDTO is used by Admin to review a seller application.
 * Admin can APPROVE, REJECT, or DEACTIVATE a seller's shop.
 */
public class SellerReviewDTO {

    private Long sellerId;                      // Which seller to approve/reject
    private SellerStatus status;                // APPROVED / REJECTED / DEACTIVATED
    private String reviewedByAdmin;             // Admin username/email
    private String reviewComment;               // Optional (Required when REJECTED)

    // ============================
    // Getters & Setters
    // ============================

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public SellerStatus getStatus() {
        return status;
    }

    public void setStatus(SellerStatus status) {
        this.status = status;
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
}
