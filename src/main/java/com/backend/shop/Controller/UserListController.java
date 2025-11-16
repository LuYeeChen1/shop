package com.backend.shop.Controller;

import com.backend.shop.Model.UserModel;
import com.backend.shop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserListController {

    private final UserService userService;

    // Constructor injection
    public UserListController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String showUsers(Model model, HttpSession session) {
        UserModel loggedInUser = (UserModel) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // Not logged in, redirect to login page with a query parameter
            return "redirect:/login?needLogin=true";
        }

        // Add logged-in user's username to the model
        model.addAttribute("loggedInUsername", loggedInUser.getUsername());

        // Add all users
        model.addAttribute("users", userService.getAllUsers());

        return "user_list";
    }


}
