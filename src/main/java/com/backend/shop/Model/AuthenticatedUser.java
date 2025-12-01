package com.backend.shop.Model;

/**
 * A simple authentication result object to represent a logged-in user.
 * This object is typically stored in the session.
 */
public class AuthenticatedUser {

    private Long userId;
    private String email;
    private String username;
    private String role; // ADMIN, CUSTOMER, SELLER, AGENT

    public AuthenticatedUser() {}

    public AuthenticatedUser(Long userId, String email, String username, String role) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
