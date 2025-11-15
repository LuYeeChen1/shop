package com.backend.shop.DataTransferObject;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

    // Username chosen by the user during registration
    // @NotBlank → The username field cannot be empty
    @NotBlank(message = "Username is required")
    private String username;

    // Email address used for account registration
    // @Email     → Ensures the input is a valid email format
    // @NotBlank  → Field must not be empty
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    // Password for the user account
    // @NotBlank  → Field must not be empty
    // @Size(min=6) → Password must be at least 6 characters long
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

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
}
