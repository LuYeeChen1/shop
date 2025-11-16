package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UserService userService;

    public RegisterService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handle the registration logic for a new user.
     * Returns the created UserModel if successful.
     * Returns null if the email is already registered.
     */
    public UserModel register(RegisterDTO registerDTO) {

        // Check if email is already in use
        if (userService.emailExists(registerDTO.getEmail())) {
            return null;
        }

        // Delegate user creation to UserService
        return userService.registerNewUser(registerDTO);
    }
}
