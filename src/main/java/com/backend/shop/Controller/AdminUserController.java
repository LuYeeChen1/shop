package com.backend.shop.Controller;

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
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/users")
    public String listUsers(HttpSession session, Model model) {
        UserModel loggedInUser = (UserModel) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        if (loggedInUser.getUserRole() != UserRole.ADMIN) {
            return "access-denied";
        }

        List<UserModel> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "user_list";
    }
}
