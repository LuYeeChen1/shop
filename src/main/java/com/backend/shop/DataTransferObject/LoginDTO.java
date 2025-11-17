package com.backend.shop.DataTransferObject;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    // Email field for login
    // @Email       → Ensures the value is a valid email format
    // @NotBlank    → Ensures the field cannot be empty
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    // Password field for login
    // @NotBlank    → Ensures the password is provided
    @NotBlank(message = "Password is required")
    private String password;

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
