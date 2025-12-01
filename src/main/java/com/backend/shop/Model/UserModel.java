package com.backend.shop.Model;

public class UserModel {

    private Long id;
    private String username;
    private String email;
    private String password;
    private UserRole userRole;
    private String sellerApplicationStatus; // NONE, PENDING, APPROVED, REJECTED

    public UserModel() {
    }

    public UserModel(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = UserRole.CUSTOMER; // Default role
        this.sellerApplicationStatus = "NONE"; // Default application status
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getSellerApplicationStatus() {
        return sellerApplicationStatus;
    }

    public void setSellerApplicationStatus(String sellerApplicationStatus) {
        this.sellerApplicationStatus = sellerApplicationStatus;
    }
}
