package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;

import java.util.List;

//Used Interface Structure
public interface UserService {

    // Register a new user and return the UserModel
    UserModel registerNewUser(RegisterDTO registerDTO);

    // Return all users saved in the system
    List<UserModel> getAllUsers();
}
