package com.backend.shop.Model;

public class AdminModel {

    private String email;       // PK
    private String username;
    private String password;

    public AdminModel() {}

    public AdminModel(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Getters & Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
