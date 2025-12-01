package com.backend.shop.Model;

public class UserModel {

    private Long id;          // maps to users.user_id
    private String email;     // maps to users.email
    private String username;  // maps to users.username
    private String password;  // maps to users.password (hashed)
    private UserRole role;    // maps to users.user_role

    public UserModel() {
    }

    public UserModel(Long id, String email, String username, String password, UserRole role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    /**
     * This stores the hashed password (not plain text).
     */
    public String getPassword() {
        return password;
    }

    /**
     * Always set the hashed password here.
     * Do not store raw passwords in this field.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
