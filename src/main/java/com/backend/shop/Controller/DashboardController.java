package com.backend.shop.Controller;

import com.backend.shop.Model.AuthenticatedUser;
import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Repository.SellerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private SellerRepository sellerRepository;

    private AuthenticatedUser getLoggedInUser(HttpSession session) {
        Object obj = session.getAttribute("loggedInUser");
        if (obj instanceof AuthenticatedUser) {
            return (AuthenticatedUser) obj;
        }
        return null;
    }

    @GetMapping("/customer/dashboard")
    public String customerDashboard(HttpSession session, Model model) {
        AuthenticatedUser user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (!"CUSTOMER".equals(user.getRole())) {
            return "access-denied";
        }

        // Load seller info (may be null if never applied)
        SellerModel seller = sellerRepository.findByUserId(user.getUserId());
        model.addAttribute("seller", seller);

        return "customer/customer_dashboard";
    }

    @GetMapping("/seller/dashboard")
    public String sellerDashboard(HttpSession session) {
        AuthenticatedUser user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (!"SELLER".equals(user.getRole())) {
            return "access-denied";
        }
        return "seller/seller_dashboard";
    }

    @GetMapping("/agent/dashboard")
    public String agentDashboard(HttpSession session) {
        AuthenticatedUser user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (!"AGENT".equals(user.getRole())) {
            return "access-denied";
        }
        return "agent/agent_dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {
        AuthenticatedUser user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(user.getRole())) {
            return "access-denied";
        }
        return "admin/admin_dashboard";
    }
}
