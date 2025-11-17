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

        // Delegate registration logic to UserService.
        // UserService will:
        //  - check if email exists
        //  - create the user with default CUSTOMER role
        //  - return null if registration fails
        UserModel createdUser = userService.registerNewUser(registerDTO);

        // If null is returned, we treat it as "email already registered"
        return createdUser;
    }
}
