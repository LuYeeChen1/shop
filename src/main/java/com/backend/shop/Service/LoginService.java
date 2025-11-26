package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.LoginDTO;
import com.backend.shop.Model.AuthenticatedUser;
import org.springframework.stereotype.Service;

/**
 * LoginService acts as the main coordinator for user login.
 * It delegates the actual authentication logic to UserService.
 * This version supports multiple user roles (Admin, Customer, Seller)
 * and returns a unified AuthenticatedUser object.
 */
@Service
public class LoginService {

    private final UserService userService;

    public LoginService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Process user login using the provided LoginDTO.
     * Delegates authentication to UserService.
     *
     * @param loginDTO Contains the email and password submitted by the user
     * @return AuthenticatedUser if credentials are valid, otherwise null
     */
    public AuthenticatedUser login(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String rawPassword = loginDTO.getPassword();

        // Delegate authentication to UserService
        return userService.authenticate(email, rawPassword);
    }
}
