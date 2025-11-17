package com.backend.shop.Controller;

import com.backend.shop.Model.UserModel;
import com.backend.shop.Service.UserAdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserListController {

    private final UserAdminService userAdminService;

    // Constructor injection
    public UserListController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @GetMapping("/users")
    public String showUsers(Model model, HttpSession session) {

        // Get logged-in user from session
        UserModel loggedInUser = (UserModel) session.getAttribute("loggedInUser");

        // 1. Not logged in → redirect to login
        if (!userAdminService.isLoggedIn(loggedInUser)) {
            return "redirect:/login?needLogin=true";
        }

        // 2. Logged in but not allowed (not ADMIN) → access denied
        if (!userAdminService.canViewUserList(loggedInUser)) {
            return "redirect:/access-denied";
        }

        // 3. Admin user → load data for user list page
        model.addAttribute("loggedInUsername", loggedInUser.getUsername());
        model.addAttribute("users", userAdminService.getAllUsersForAdmin());

        // Thymeleaf view name
        return "user_list";
    }
}
