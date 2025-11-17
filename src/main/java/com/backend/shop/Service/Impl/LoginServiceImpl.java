package com.backend.shop.Service.Impl;

import com.backend.shop.DataTransferObject.LoginDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Service.Interface.UserService;
import org.springframework.stereotype.Service;

//Service Is Brain

@Service
public class LoginServiceImpl {

    private final UserService userService;

    public LoginServiceImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handle the login logic for a user.
     * Returns the authenticated UserModel if successful, otherwise null.
     */
    public UserModel login(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        // Delegate authentication to UserService
        return userService.authenticate(email, password);
    }
}
