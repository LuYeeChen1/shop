package com.backend.shop.Controller;

import com.backend.shop.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserListController {

    private final UserService userService;

    // Constructor injection for UserService
    public UserListController(UserService userService) {
        this.userService = userService;
    }

    // Display the list of all users
    // NOTE:
    //  - Currently, this endpoint is PUBLIC. Anyone can access /users.
    //  - In the future (when database + roles are added), this will be changed
    //    to restrict access to ADMIN users only.
    @GetMapping("/users")
    public String showUsers(Model model) {

        // Fetch all users from the service layer
        // The "users" attribute will be displayed in user_list.html
        model.addAttribute("users", userService.getAllUsers());

        return "user_list"; // returns user_list.html
    }
}
