package com.backend.shop.Controller;

import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private UserModel getLoggedInUser(HttpSession session) {
        Object obj = session.getAttribute("loggedInUser");
        if (obj instanceof UserModel) {
            return (UserModel) obj;
        }
        return null;
    }

    @GetMapping("/customer/dashboard")
    public String customerDashboard(HttpSession session) {
        UserModel user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getUserRole() != UserRole.CUSTOMER) {
            return "access-denied";
        }
        return "customer/customer_dashboard";
    }

    @GetMapping("/seller/dashboard")
    public String sellerDashboard(HttpSession session) {
        UserModel user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getUserRole() != UserRole.SELLER) {
            return "access-denied";
        }
        return "seller/seller_dashboard";
    }

    @GetMapping("/agent/dashboard")
    public String agentDashboard(HttpSession session) {
        UserModel user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getUserRole() != UserRole.AGENT) {
            return "access-denied";
        }
        return "agent/agent_dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {
        UserModel user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getUserRole() != UserRole.ADMIN) {
            return "access-denied";
        }
        return "admin/admin_dashboard";
    }
}
