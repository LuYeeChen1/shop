package com.backend.shop.Controller;

import com.backend.shop.Model.AuthenticatedUser;
import com.backend.shop.Service.UserAdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * UserListController is responsible for displaying the list of users.
 * Only ADMIN users are allowed to access this page.
 */
@Controller
public class UserListController {

    private final UserAdminService userAdminService;

    // Constructor injection
    public UserListController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    /**
     * Show the user list page.
     * Requires the user to be logged in and have ADMIN privileges.
     */
    @GetMapping("/users")
    public String showUsers(Model model, HttpSession session) {

        // Get logged-in user from session (stored as AuthenticatedUser)
        AuthenticatedUser loggedInUser =
                (AuthenticatedUser) session.getAttribute("loggedInUser");

        // 1. Not logged in → redirect to login page with info message
        if (!userAdminService.isLoggedIn(loggedInUser)) {
            return "redirect:/login?needLogin=true";
        }

        // 2. Logged in but not ADMIN → redirect to access denied page
        if (!userAdminService.canViewUserList(loggedInUser)) {
            return "redirect:/access-denied";
        }

        // 3. User is ADMIN → load data for the user list page
        model.addAttribute("loggedInUsername", loggedInUser.getUsername());
        model.addAttribute("users", userAdminService.getAllUsersForAdmin());

        // Thymeleaf template name: user_list.html
        return "user_list";
    }
}
