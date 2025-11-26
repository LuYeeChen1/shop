package com.backend.shop.Model;

// A simple auth result object to represent a logged-in user
public class AuthenticatedUser {

    private String email;
    private String username;
    private String role; // "ADMIN", "CUSTOMER", "SELLER"

    public AuthenticatedUser() {}

    public AuthenticatedUser(String email, String username, String role) {
        this.email = email;
        this.username = username;
        this.role = role;
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
