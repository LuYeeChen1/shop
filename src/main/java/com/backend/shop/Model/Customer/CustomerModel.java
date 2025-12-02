package com.backend.shop.Model.Customer;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * CustomerModel stores customer-specific profile data.
 * It maps to the "customers" table and references the users table via user_id.
 */
@Entity
@Table(name = "customers")
public class CustomerModel {

    // Primary Key + Foreign Key to users.id
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "full_name", length = 200)
    private String fullName;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(name = "default_shipping_address", length = 500)
    private String defaultShippingAddress;

    @Column(name = "billing_address", length = 500)
    private String billingAddress;

    @Column(name = "preferred_language", length = 50)
    private String preferredLanguage;

    @Column(name = "preferred_currency", length = 20)
    private String preferredCurrency;

    @Column(name = "loyalty_points")
    private int loyaltyPoints;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor
    public CustomerModel() {
    }


    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDefaultShippingAddress() {
        return defaultShippingAddress;
    }

    public void setDefaultShippingAddress(String defaultShippingAddress) {
        this.defaultShippingAddress = defaultShippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
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
