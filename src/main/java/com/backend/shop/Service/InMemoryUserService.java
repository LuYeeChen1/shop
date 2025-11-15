package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InMemoryUserService implements UserService {

    // Acts as an in-memory database.
    // This list stores all registered users during the runtime of the application.
    private final List<UserModel> users = new ArrayList<>();

    // Registers a new user using data from RegisterDTO
    @Override
    public UserModel registerNewUser(RegisterDTO registerDTO) {

        // Convert DTO to UserModel (domain model)
        UserModel userModel = new UserModel(
                registerDTO.getUsername(),
                registerDTO.getEmail(),
                registerDTO.getPassword()
        );

        // Store the user in the in-memory list
        users.add(userModel);

        // Return created user object
        return userModel;
    }

    // Returns all users stored in memory
    @Override
    public List<UserModel> getAllUsers() {
        return users;
    }

    // Authenticate by checking email + password
    // Very simple logic — works only for demo purposes
    @Override
    public UserModel authenticate(String email, String password) {

        // Loop through all registered users
        for (UserModel user : users) {

            // Check for matching email and password
            if (user.getEmail().equalsIgnoreCase(email)
                    && user.getPassword().equals(password)) {
                return user; // Authentication success
            }
        }

        // No matching user found → authentication failed
        return null;
    }
}
