package com.backend.shop.Controller.AdminController;

import com.backend.shop.Model.Session.AuthenticatedUser;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminUserListController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/users")
    public String listUsers(HttpSession session, Model model) {

        // Get logged-in user from session
        AuthenticatedUser logged = (AuthenticatedUser) session.getAttribute("loggedInUser");

        if (logged == null) {
            return "redirect:/login";
        }

        // Ensure only ADMIN can access
        if (!"ADMIN".equals(logged.getRole())) {
            return "access-denied";
        }

        // Load all base users from users table
        List<UserModel> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "admin/user_list";
    }
}
