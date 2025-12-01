package com.backend.shop.Controller.CustomerController;

import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;
import com.backend.shop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CustomerSellerController {

    @Autowired
    private UserService userService;

    private UserModel getLoggedInUser(HttpSession session) {
        Object obj = session.getAttribute("loggedInUser");
        if (obj instanceof UserModel) {
            return (UserModel) obj;
        }
        return null;
    }

    @PostMapping("/customer/apply-seller")
    public String applySeller(HttpSession session) {

        UserModel user = getLoggedInUser(session);
        if (user == null) return "redirect:/login";
        if (user.getUserRole() != UserRole.CUSTOMER) return "access-denied";

        if (user.getSellerApplicationStatus().equals("NONE")
                || user.getSellerApplicationStatus().equals("REJECTED")) {

            user.setSellerApplicationStatus("PENDING");
        }

        return "redirect:/customer/dashboard";
    }

    @PostMapping("/customer/enter-seller")
    public String enterSeller(HttpSession session) {

        UserModel user = getLoggedInUser(session);
        if (user == null) return "redirect:/login";

        if (user.getSellerApplicationStatus().equals("APPROVED")) {
            user.setUserRole(UserRole.SELLER);
            return "redirect:/seller/dashboard";
        }

        return "access-denied";
    }
}
