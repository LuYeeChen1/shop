package com.backend.shop.Model;

public class UserModel {

    // Represents the user's chosen username
    private String username;

    // Represents the user's email address (unique identifier for login)
    private String email;

    // Represents the user's password (in real applications, should be hashed)
    private String password;

    // Represents the role of the user (e.g. ADMIN, CUSTOMER)
    private UserRole userRole;

    // Default constructor required by Spring and frameworks
    public UserModel() {
    }

    // Constructor used when creating a new user object
    public UserModel(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for userRole
    public UserRole getUserRole() {
        return userRole;
    }

    // Setter for userRole
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
