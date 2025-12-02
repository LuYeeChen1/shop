package com.backend.shop.Model.Admin;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * AdminModel represents admin-specific account and permissions.
 * It is mapped to the "admins" table in the database.
 */
@Entity
@Table(name = "admins")
public class AdminModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "username", nullable = false, unique = true, length = 200)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "full_name", length = 200)
    private String fullName;

    @Column(name = "contact_email", length = 200)
    private String contactEmail;

    @Column(name = "contact_number", length = 50)
    private String contactNumber;

    @Column(name = "can_approve_seller")
    private boolean canApproveSeller;

    @Column(name = "can_manage_products")
    private boolean canManageProducts;

    @Column(name = "can_view_reports")
    private boolean canViewReports;

    @Column(name = "can_manage_agents")
    private boolean canManageAgents;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public AdminModel() {
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public boolean isCanApproveSeller() {
        return canApproveSeller;
    }

    public void setCanApproveSeller(boolean canApproveSeller) {
        this.canApproveSeller = canApproveSeller;
    }

    public boolean isCanManageProducts() {
        return canManageProducts;
    }

    public void setCanManageProducts(boolean canManageProducts) {
        this.canManageProducts = canManageProducts;
    }

    public boolean isCanViewReports() {
        return canViewReports;
    }

    public void setCanViewReports(boolean canViewReports) {
        this.canViewReports = canViewReports;
    }

    public boolean isCanManageAgents() {
        return canManageAgents;
    }

    public void setCanManageAgents(boolean canManageAgents) {
        this.canManageAgents = canManageAgents;
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
