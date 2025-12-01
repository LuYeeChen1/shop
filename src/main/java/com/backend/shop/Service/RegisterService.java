package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;
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
     * @param role        expected to be CUSTOMER or SELLER
     * @return the created base user model (from users table), or null if registration failed
     */
    public UserModel register(RegisterDTO registerDTO, String role) {

        if (role == null) {
            return null;
        }

        // Normalize role to uppercase
        role = role.toUpperCase();

        // ADMIN is forbidden in public registration
        if ("ADMIN".equals(role)) {
            return null; // reject admin registration
        }

        // Map string role to UserRole enum
        UserRole userRole;
        switch (role) {
            case "SELLER":
                userRole = UserRole.SELLER;
                break;
            case "CUSTOMER":
            default:
                userRole = UserRole.CUSTOMER;
                break;
        }

        // Delegate to UserService:
        //  - check if email exists in users table
        //  - hash password
        //  - insert into users table with given role
        //  - return created UserModel or null if failed
        return userService.registerNewUser(registerDTO, userRole);
    }
}
