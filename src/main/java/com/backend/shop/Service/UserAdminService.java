package com.backend.shop.Service;

import com.backend.shop.Model.Session.AuthenticatedUser;
import com.backend.shop.Model.UserModel;
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
        return user != null && "ADMIN".equals(user.getRole());
    }

    /**
     * Get a list of all users from the users table.
     * This is intended for ADMIN views (e.g. /admin/users page).
     *
     * @return list of UserModel representing all users
     */
    public List<UserModel> getAllUsersForAdmin() {
        return userService.getAllUsers();
    }
}
