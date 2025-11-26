package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;

import java.util.List;

// This interface defines user-related operations
public interface UserService {

    // Register a new user with default role
    UserModel registerNewUser(RegisterDTO registerDTO);

    // Authenticate user by email and password
    UserModel authenticate(String email, String password);

    // Check if email already exists
    boolean emailExists(String email);

    // Get all users (for listing)
    List<UserModel> getAllUsers();

    // Check if the user is an admin
    boolean isAdmin(UserModel user);
}
