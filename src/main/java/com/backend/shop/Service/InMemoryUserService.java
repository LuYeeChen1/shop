package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InMemoryUserService implements UserService {

    // This list works as an in-memory database
    private final List<UserModel> users = new ArrayList<>();

    public InMemoryUserService() {
        // Create a default admin account when the application starts
        RegisterDTO adminDTO = new RegisterDTO();
        adminDTO.setUsername("admin");
        adminDTO.setEmail("admin@shop.com");
        adminDTO.setPassword("123456");

        registerNewUser(adminDTO, UserRole.ADMIN);
    }

    @Override
    public UserModel registerNewUser(RegisterDTO registerDTO) {
        // Default role: CUSTOMER
        return registerNewUser(registerDTO, UserRole.CUSTOMER);
    }

    @Override
    public UserModel registerNewUser(RegisterDTO registerDTO, UserRole role) {

        // Check if email already exists
        if (emailExists(registerDTO.getEmail())) {
            // Email already registered, return null to indicate failure
            return null;
        }

        UserModel userModel = new UserModel(
                registerDTO.getUsername(),
                registerDTO.getEmail(),
                registerDTO.getPassword()
        );

        // Set the role for the new user
        userModel.setUserRole(role);

        users.add(userModel);
        return userModel;
    }

    @Override
    public List<UserModel> getAllUsers() {
        return users;
    }

    @Override
    public UserModel authenticate(String email, String password) {
        // Simple authentication: check email and password match
        for (UserModel user : users) {
            if (user.getEmail().equalsIgnoreCase(email)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }
        // No matching user found
        return null;
    }

    @Override
    public boolean emailExists(String email) {
        for (UserModel user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAdmin(UserModel user) {
        return user != null && user.getUserRole() == UserRole.ADMIN;
    }
}
