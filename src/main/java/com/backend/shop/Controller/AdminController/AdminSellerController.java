package com.backend.shop.Controller.AdminController;

import com.backend.shop.Model.Session.AuthenticatedUser;
import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Repository.Seller.SellerRepository;
import com.backend.shop.Service.SellerService.SellerAdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminSellerController {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SellerAdminService sellerAdminService;

    // Helper: get logged-in user from session
    private AuthenticatedUser getLoggedInUser(HttpSession session) {
        Object obj = session.getAttribute("loggedInUser");
        if (obj instanceof AuthenticatedUser) {
            return (AuthenticatedUser) obj;
        }
        return null;
    }

    /**
     * Show all PENDING seller applications to admin.
     */
    @GetMapping("/admin/seller-applications")
    public String viewSellerApplications(HttpSession session, Model model) {

        AuthenticatedUser admin = getLoggedInUser(session);
        if (admin == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(admin.getRole())) {
            return "access-denied";
        }

        List<SellerModel> applications = sellerRepository.findByStatus(SellerStatus.PENDING);
        model.addAttribute("applications", applications);

        return "admin/admin_seller_review";
    }

    /**
     * Approve a seller application.
     */
    @PostMapping("/admin/seller-approve/{userId}")
    public String approveSeller(
            @PathVariable Long userId,
            HttpSession session
    ) {
        AuthenticatedUser admin = getLoggedInUser(session);
        if (admin == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(admin.getRole())) {
            return "access-denied";
        }

        sellerAdminService.approveSeller(userId, admin.getEmail(), "Approved");

        return "redirect:/admin/seller-applications";
    }

    /**
     * Reject a seller application with an optional comment.
     */
    @PostMapping("/admin/seller-reject/{userId}")
    public String rejectSeller(
            @PathVariable Long userId,
            @RequestParam(name = "comment", required = false) String comment,
            HttpSession session
    ) {
        AuthenticatedUser admin = getLoggedInUser(session);
        if (admin == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(admin.getRole())) {
            return "access-denied";
        }

        sellerAdminService.rejectSeller(userId, admin.getEmail(), comment);

        return "redirect:/admin/seller-applications";
    }

    /**
     * Show all sellers (any status) to admin.
     */
    @GetMapping("/admin/seller-list")
    public String viewAllSellers(HttpSession session, Model model) {

        AuthenticatedUser admin = getLoggedInUser(session);
        if (admin == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(admin.getRole())) {
            return "access-denied";
        }

        List<SellerModel> sellers = sellerRepository.findAll();
        model.addAttribute("sellers", sellers);

        return "admin/admin_seller_list";
    }

    /**
     * Deactivate an approved seller.
     */
    @PostMapping("/admin/seller-deactivate/{userId}")
    public String deactivateSeller(
            @PathVariable Long userId,
            HttpSession session
    ) {
        AuthenticatedUser admin = getLoggedInUser(session);
        if (admin == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(admin.getRole())) {
            return "access-denied";
        }

        sellerAdminService.deactivateSeller(userId, admin.getEmail());

        return "redirect:/admin/seller-list";
    }
}
