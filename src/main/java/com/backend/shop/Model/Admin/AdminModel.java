package com.backend.shop.Model.Admin;

public class AdminModel {

    private Long userId;

    private String fullName;
    private String contactEmail;
    private String contactNumber;

    private boolean canApproveSeller;
    private boolean canManageProducts;
    private boolean canViewReports;
    private boolean canManageAgents;

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
}
