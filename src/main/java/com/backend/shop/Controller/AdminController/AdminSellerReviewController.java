package com.backend.shop.Controller.AdminController;

import com.backend.shop.Model.AuthenticatedUser;
import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Repository.SellerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AdminSellerReviewController {

    @Autowired
    private SellerRepository sellerRepository;

    /**
     * Get the currently logged-in user from session.
     * Session is expected to store an AuthenticatedUser instance
     * under the attribute name "loggedInUser".
     */
    private AuthenticatedUser getLoggedInUser(HttpSession session) {
        Object obj = session.getAttribute("loggedInUser");
        if (obj instanceof AuthenticatedUser) {
            return (AuthenticatedUser) obj;
        }
        return null;
    }

    /**
     * Show all pending seller applications to admin.
     */
    @GetMapping("/admin/seller-applications")
    public String viewSellerApplications(HttpSession session, Model model) {

        AuthenticatedUser admin = getLoggedInUser(session);
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            return "access-denied";
        }

        // Load all sellers with PENDING status
        List<SellerModel> applications =
                sellerRepository.findByStatus(SellerStatus.PENDING);

        model.addAttribute("applications", applications);
        return "admin/admin_seller_review";
    }

    /**
     * Approve a seller application by userId.
     */
    @PostMapping("/admin/seller-approve/{userId}")
    public String approveSeller(@PathVariable("userId") Long userId,
                                HttpSession session) {

        AuthenticatedUser admin = getLoggedInUser(session);
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            return "access-denied";
        }

        SellerModel seller = sellerRepository.findByUserId(userId);
        if (seller != null) {
            seller.setStatus(SellerStatus.APPROVED);
            seller.setReviewedAt(LocalDateTime.now());
            seller.setReviewedByAdmin(admin.getEmail());
            seller.setReviewComment("Approved by admin");
            sellerRepository.update(seller);
        }

        return "redirect:/admin/seller-applications";
    }

    /**
     * Reject a seller application by userId.
     */
    @PostMapping("/admin/seller-reject/{userId}")
    public String rejectSeller(@PathVariable("userId") Long userId,
                               HttpSession session) {

        AuthenticatedUser admin = getLoggedInUser(session);
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            return "access-denied";
        }

        SellerModel seller = sellerRepository.findByUserId(userId);
        if (seller != null) {
            seller.setStatus(SellerStatus.REJECTED);
            seller.setReviewedAt(LocalDateTime.now());
            seller.setReviewedByAdmin(admin.getEmail());
            seller.setReviewComment("Rejected by admin");
            sellerRepository.update(seller);
        }

        return "redirect:/admin/seller-applications";
    }
}
