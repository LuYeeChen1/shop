package com.backend.shop.Controller.AdminController;

import com.backend.shop.Model.Session.AuthenticatedUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @GetMapping("/dashboard")
    public String showAdminDashboard(HttpSession session, Model model) {

        // Read admin from session
        AuthenticatedUser loggedInAdmin =
                (AuthenticatedUser) session.getAttribute("loggedInAdmin");

        // If no admin in session, redirect to admin login
        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }

        // Add admin info to page
        model.addAttribute("admin", loggedInAdmin);

        // Must match templates/admin/admin_dashboard.html
        return "admin/admin_dashboard";
    }

    @GetMapping("/logout")
    public String logoutAdmin(HttpSession session) {
        // Remove admin from session
        session.removeAttribute("loggedInAdmin");

        // Redirect to admin login
        return "redirect:/admin/login?logout";
    }
}
