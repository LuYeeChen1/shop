package com.backend.shop.Controller.AdminController;

import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;
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

        UserModel user = (UserModel) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        if (user.getUserRole() != UserRole.ADMIN) {
            return "access-denied";
        }

        List<UserModel> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "admin/user_list";
    }
}
