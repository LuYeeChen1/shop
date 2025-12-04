package com.backend.shop.DataTransferObject.Seller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO used when a customer applies to become a seller.
 * Collects only the information required from the user.
 */
public class SellerApplicationDTO {

    @NotBlank(message = "Shop name is required")
    @Size(max = 255, message = "Shop name must not exceed 255 characters")
    private String shopName;

    @NotBlank(message = "Shop description is required")
    private String shopDescription;

    // Optional: customer can provide a logo URL (or later we upload to S3)
    @Size(max = 500, message = "Shop logo URL must not exceed 500 characters")
    private String shopLogoUrl;

    @NotBlank(message = "Business registration number is required")
    @Size(max = 100, message = "Business registration number must not exceed 100 characters")
    private String businessRegistrationNumber;

    @NotBlank(message = "Business address is required")
    private String businessAddress;

    @NotBlank(message = "Contact number is required")
    @Size(max = 50, message = "Contact number must not exceed 50 characters")
    private String contactNumber;

    // ===== Getters and Setters =====

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
