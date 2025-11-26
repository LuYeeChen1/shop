package com.backend.shop.Service;

import com.backend.shop.Model.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserAdminService provides helper methods for admin-related checks
 * and fetching user data for administrative views.
 */
@Service
public class UserAdminService {

    private final UserService userService;

    public UserAdminService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Check if there is a logged-in user in the current session.
     *
     * @param user the AuthenticatedUser retrieved from session
     * @return true if user is not null, otherwise false
     */
    public boolean isLoggedIn(AuthenticatedUser user) {
        return user != null;
    }

    /**
     * Check whether the given user is allowed to view the user list.
     * Only ADMIN users should have this permission.
     *
     * @param user the AuthenticatedUser retrieved from session
     * @return true if user has ADMIN role, otherwise false
     */
    public boolean canViewUserList(AuthenticatedUser user) {
        return userService.isAdmin(user);
    }

    /**
     * Get a summary list of all users from all role tables.
     * This is intended for ADMIN views (e.g. /users page).
     *
     * @return list of AuthenticatedUser representing all users
     */
    public List<AuthenticatedUser> getAllUsersForAdmin() {
        return userService.getAllUsersSummary();
    }
}
