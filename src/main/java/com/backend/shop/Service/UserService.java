package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.AdminModel;
import com.backend.shop.Model.CustomerModel;
import com.backend.shop.Model.SellerModel;
import com.backend.shop.Model.AuthenticatedUser;

import java.util.List;

public interface UserService {

    // Register for different roles
    AdminModel registerAdmin(RegisterDTO registerDTO);

    CustomerModel registerCustomer(RegisterDTO registerDTO);

    SellerModel registerSeller(RegisterDTO registerDTO);

    // Login: auto-detect which table the email belongs to
    AuthenticatedUser authenticate(String email, String rawPassword);

    // Check if email already used in any table
    boolean emailExists(String email);

    // Optional: get summary of all users in all role-tables
    List<AuthenticatedUser> getAllUsersSummary();

    // Check role
    boolean isAdmin(AuthenticatedUser user);
}
