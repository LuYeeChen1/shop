package com.backend.shop.Controller;

import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;
import com.backend.shop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RoleSwitchController {

    @Autowired
    private UserService userService;

    @PostMapping("/switch-to-seller")
    public String switchToSeller(HttpSession session) {
        UserModel loggedInUser = (UserModel) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        userService.changeUserRole(loggedInUser, UserRole.SELLER);

        return "redirect:/seller/dashboard";
    }

    @PostMapping("/switch-to-customer")
    public String switchToCustomer(HttpSession session) {
        UserModel loggedInUser = (UserModel) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        userService.changeUserRole(loggedInUser, UserRole.CUSTOMER);

        return "redirect:/customer/dashboard";
    }
}
