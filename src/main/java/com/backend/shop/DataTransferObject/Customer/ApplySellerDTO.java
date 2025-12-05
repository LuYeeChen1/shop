package com.backend.shop.DataTransferObject.Customer;

/**
 * ApplySellerDTO is used when a customer applies to open a new shop.
 * It only contains fields that the customer is allowed to submit.
 */
public class ApplySellerDTO {

    // Which user is applying to open this shop (FK to users.user_id)
    private Long userId;

    // Seller email (usually same as the user's email)
    private String email;

    private String shopName;
    private String shopDescription;
    private String shopLogoUrl;

    private String businessRegistrationNumber;
    private String businessAddress;
    private String contactNumber;

    // ============================
    // Getters & Setters
    // ============================

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
}
