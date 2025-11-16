package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;

import java.util.List;

public interface UserService {

    /**
     * Register a new user with default role (CUSTOMER).
     * Returns the created UserModel, or null if the email is already registered.
     */
    UserModel registerNewUser(RegisterDTO registerDTO);

    /**
     * Register a new user with a specific role (e.g. ADMIN, CUSTOMER).
     * Returns the created UserModel, or null if the email is already registered.
     */
    UserModel registerNewUser(RegisterDTO registerDTO, UserRole role);

    /**
     * Get a list of all registered users.
     */
    List<UserModel> getAllUsers();

    /**
     * Authenticate a user by email and password.
     * Returns the UserModel if valid, otherwise null.
     */
    UserModel authenticate(String email, String password);

    /**
     * Check if an email is already registered.
     * Returns true if email exists, otherwise false.
     */
    boolean emailExists(String email);

    /**
     * Check whether a given user has ADMIN role.
     */
    boolean isAdmin(UserModel user);
}
