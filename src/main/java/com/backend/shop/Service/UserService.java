package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;

import java.util.List;

public interface UserService {

    // Register a new user and return the UserModel
    UserModel registerNewUser(RegisterDTO registerDTO);

    // Return all users saved in the system
    List<UserModel> getAllUsers();

    // Authenticate user by email and password
    UserModel authenticate(String email, String password);
}
