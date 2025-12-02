package com.backend.shop.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a system user.
 * This entity maps to the "users" table.
 * All roles (Admin, Seller, Customer, Agent) extend from this base user.
 */
@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")   // Primary key
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 200)
    private String email;

    @Column(name = "username", nullable = false, length = 200)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;    // Stored as BCrypt hash

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private UserRole role;      // BASIC ROLE ONLY

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // Default constructor
    public UserModel() {}

    // Full constructor
    public UserModel(Long id, String email, String username, String password, UserRole role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }


    // Getters and Setters

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

    public String getPassword() {
        return password;
    }

    /**
     * Password stored must ALWAYS be hashed.
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
