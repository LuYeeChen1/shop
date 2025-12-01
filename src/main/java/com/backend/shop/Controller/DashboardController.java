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

    private boolean hasRole(UserModel user, UserRole role) {
        return user != null && user.getUserRole() == role;
    }

    @GetMapping("/customer/dashboard")
    public String customerDashboard(HttpSession session) {
        UserModel user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (!hasRole(user, UserRole.CUSTOMER)) {
            return "access-denied";
        }
        return "dashboard/customer_dashboard";
    }

    @GetMapping("/seller/dashboard")
    public String sellerDashboard(HttpSession session) {
        UserModel user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (!hasRole(user, UserRole.SELLER)) {
            return "access-denied";
        }
        return "dashboard/seller_dashboard";
    }

    @GetMapping("/agent/dashboard")
    public String agentDashboard(HttpSession session) {
        UserModel user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (!hasRole(user, UserRole.AGENT)) {
            return "access-denied";
        }
        return "dashboard/agent_dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {
        UserModel user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (!hasRole(user, UserRole.ADMIN)) {
            return "access-denied";
        }
        return "dashboard/admin_dashboard";
    }
}
