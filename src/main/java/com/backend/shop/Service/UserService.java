package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.Session.AuthenticatedUser;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;

import java.util.List;

public interface UserService {

    /**
     * Register new base user in users table.
     * Does NOT create admin/customer/seller profile.
     */
    UserModel registerNewUser(RegisterDTO registerDTO);

    /**
     * Register with a specific role.
     */
    UserModel registerNewUser(RegisterDTO registerDTO, UserRole role);

    /**
     * Authenticate by checking users table only.
     * Returns a safe AuthenticatedUser object for session usage.
     */
    AuthenticatedUser authenticate(String email, String rawPassword);

    /**
     * Check if email is already used in users table.
     */
    boolean emailExists(String email);

    /**
     * Check if the user is admin based on users.user_role.
     */
    boolean isAdmin(UserModel user);

    /**
     * Change user_role in users table.
     */
    UserModel changeUserRole(Long userId, UserRole newRole);

    /**
     * Find base user info from users table by email.
     */
    UserModel findByEmail(String email);

    /**
     * Find base user info from users table by user_id.
     */
    UserModel findById(Long userId);

    /**
     * Get all users from users table.
     */
    List<UserModel> getAllUsers();
}
