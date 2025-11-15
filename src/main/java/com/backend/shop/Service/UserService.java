package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;

import java.util.List;

public interface UserService {

    /**
     * Register a new user using the data provided in RegisterDTO.
     * This method should create a UserModel object and store it (in-memory or database).
     *
     * @param registerDTO The data submitted by the user during registration.
     * @return The created UserModel instance.
     */
    UserModel registerNewUser(RegisterDTO registerDTO);

    /**
     * Retrieve all users stored in the system.
     * In production, this would likely be limited or restricted (e.g., admin only).
     *
     * @return A list of UserModel objects.
     */
    List<UserModel> getAllUsers();

    /**
     * Authenticate the user using email and password.
     * Should return a UserModel if the credentials are valid, or null if not.
     *
     * @param email    The email entered by the user.
     * @param password The password entered by the user.
     * @return The authenticated UserModel, or null if authentication fails.
     */
    UserModel authenticate(String email, String password);
}
