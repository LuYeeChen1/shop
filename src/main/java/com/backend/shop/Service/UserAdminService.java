package com.backend.shop.Service;

import com.backend.shop.Model.UserModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAdminService {

    private final UserService userService;

    public UserAdminService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Check whether there is a logged-in user.
     */
    public boolean isLoggedIn(UserModel user) {
        return user != null;
    }

    /**
     * Check whether the given user is allowed to view the user list.
     * For now, only ADMIN users are allowed.
     */
    public boolean canViewUserList(UserModel user) {
        return userService.isAdmin(user);
    }

    /**
     * Get all users for admin view.
     * Assumes permission is already checked before calling.
     */
    public List<UserModel> getAllUsersForAdmin() {
        return userService.getAllUsers();
    }
}
