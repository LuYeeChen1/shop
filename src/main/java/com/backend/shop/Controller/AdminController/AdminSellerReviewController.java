package com.backend.shop.Controller.AdminController;

import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;
import com.backend.shop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminSellerReviewController {

    @Autowired
    private UserService userService;

    private UserModel getLoggedInUser(HttpSession session) {
        Object obj = session.getAttribute("loggedInUser");
        if (obj instanceof UserModel) {
            return (UserModel) obj;
        }
        return null;
    }

    @GetMapping("/admin/seller-applications")
    public String viewSellerApplications(HttpSession session, Model model) {

        UserModel admin = getLoggedInUser(session);
        if (admin == null || admin.getUserRole() != UserRole.ADMIN) {
            return "access-denied";
        }

        List<UserModel> applications = userService.getAllUsers().stream()
                .filter(u -> !"NONE".equals(u.getSellerApplicationStatus()))
                .collect(Collectors.toList());

        model.addAttribute("applications", applications);
        return "admin/admin_seller_review";
    }

    @PostMapping("/admin/seller-approve/{id}")
    public String approveSeller(@PathVariable Long id, HttpSession session) {

        UserModel admin = getLoggedInUser(session);
        if (admin == null || admin.getUserRole() != UserRole.ADMIN) {
            return "access-denied";
        }

        UserModel user = userService.findById(id);
        if (user != null) {
            user.setSellerApplicationStatus("APPROVED");
        }

        return "redirect:/admin/seller-applications";
    }

    @PostMapping("/admin/seller-reject/{id}")
    public String rejectSeller(@PathVariable Long id, HttpSession session) {

        UserModel admin = getLoggedInUser(session);
        if (admin == null || admin.getUserRole() != UserRole.ADMIN) {
            return "access-denied";
        }

        UserModel user = userService.findById(id);
        if (user != null) {
            user.setSellerApplicationStatus("REJECTED");
        }

        return "redirect:/admin/seller-applications";
    }
}
