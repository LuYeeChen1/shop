package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.CustomerModel;
import com.backend.shop.Model.SellerModel;
import org.springframework.stereotype.Service;

/**
 * RegisterService handles registration requests and delegates
 * the actual creation of accounts to UserService.
 * Only CUSTOMER and SELLER registrations are allowed.
 */
@Service
public class RegisterService {

    private final UserService userService;

    public RegisterService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Process registration based on the specified role.
     * ADMIN registration is not allowed from the public side.
     *
     * @param registerDTO the form data submitted by the user
     * @param role        CUSTOMER or SELLER
     * @return the created user model, or null if registration failed
     */
    public Object register(RegisterDTO registerDTO, String role) {

        // Normalize role to uppercase
        role = role.toUpperCase();

        // ADMIN is forbidden in public registration
        if ("ADMIN".equals(role)) {
            return null; // reject admin registration
        }

        // Let UserService handle email checks & password hashing
        switch (role) {

            case "SELLER":
                SellerModel seller = userService.registerSeller(registerDTO);
                return seller; // could be null if failed

            case "CUSTOMER":
            default:
                CustomerModel customer = userService.registerCustomer(registerDTO);
                return customer;
        }
    }
}
