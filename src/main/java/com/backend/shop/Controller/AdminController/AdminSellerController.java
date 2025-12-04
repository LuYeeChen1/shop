package com.backend.shop.Controller.AdminController;

import com.backend.shop.DataTransferObject.Seller.SellerReviewDTO;
import com.backend.shop.Model.Session.AuthenticatedUser;
import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Repository.Seller.SellerRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AdminSellerController {

    private final SellerRepository sellerRepository;

    public AdminSellerController(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    // Helper: get admin from session
    private AuthenticatedUser getLoggedInAdmin(HttpSession session) {
        Object obj = session.getAttribute("loggedInAdmin");
        if (obj instanceof AuthenticatedUser admin) {
            if ("ADMIN".equals(admin.getRole())) {
                return admin;
            }
        }
        return null;
    }

    /**
     * List all PENDING seller applications.
     */
    @GetMapping("/admin/seller-applications")
    public String viewPendingApplications(HttpSession session, Model model) {

        AuthenticatedUser admin = getLoggedInAdmin(session);
        if (admin == null) {
            return "redirect:/admin/login";
        }

        List<SellerModel> pendingList =
                sellerRepository.findByStatus(SellerStatus.PENDING);

        model.addAttribute("admin", admin);
        model.addAttribute("applications", pendingList);

        return "admin/admin_seller_review_list";
    }

    /**
     * Show review form for single seller.
     */
    @GetMapping("/admin/seller-review/{userId}")
    public String showReviewForm(
            @PathVariable Long userId,
            HttpSession session,
            Model model
    ) {

        AuthenticatedUser admin = getLoggedInAdmin(session);
        if (admin == null) {
            return "redirect:/admin/login";
        }

        SellerModel seller = sellerRepository.findByUserId(userId);
        if (seller == null) {
            return "redirect:/admin/seller-applications";
        }

        SellerReviewDTO dto = new SellerReviewDTO();
        dto.setUserId(seller.getUserId());
        dto.setEmail(seller.getEmail());
        dto.setShopName(seller.getShopName());
        dto.setCurrentStatus(
                seller.getStatus() != null ? seller.getStatus().name() : "NONE"
        );
        dto.setDecision("");
        dto.setReviewComment(seller.getReviewComment());

        model.addAttribute("reviewDTO", dto);
        model.addAttribute("admin", admin);

        return "admin/admin_seller_review_form";
    }

    /**
     * Handle Approve/Reject (NO SERVICE, direct repository update).
     */
    @PostMapping("/admin/seller-review")
    public String processReview(
            @Valid @ModelAttribute("reviewDTO") SellerReviewDTO dto,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {

        AuthenticatedUser admin = getLoggedInAdmin(session);
        if (admin == null) {
            return "redirect:/admin/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("admin", admin);
            return "admin/admin_seller_review_form";
        }

        String decision = dto.getDecision();
        String comment = dto.getReviewComment();
        Long userId = dto.getUserId();

        // Default comment fallback
        if (comment == null || comment.isBlank()) {
            comment = decision + " by " + admin.getEmail() +
                    " on " + LocalDateTime.now();
        }

        // APPROVE
        if ("APPROVE".equalsIgnoreCase(decision)) {
            sellerRepository.updateStatus(
                    userId,
                    SellerStatus.APPROVED,
                    admin.getEmail(),
                    comment
            );
        }
        // REJECT
        else if ("REJECT".equalsIgnoreCase(decision)) {
            sellerRepository.updateStatus(
                    userId,
                    SellerStatus.REJECTED,
                    admin.getEmail(),
                    comment
            );
        }
        else {
            bindingResult.rejectValue("decision", "invalid", "Invalid decision");
            model.addAttribute("admin", admin);
            return "admin/admin_seller_review_form";
        }

        return "redirect:/admin/seller-applications";
    }

    /**
     * Show all sellers (any status).
     */
    @GetMapping("/admin/seller-list")
    public String viewAllSellers(
            HttpSession session,
            Model model
    ) {
        AuthenticatedUser admin = getLoggedInAdmin(session);
        if (admin == null) {
            return "redirect:/admin/login";
        }

        List<SellerModel> all = sellerRepository.findAll();

        model.addAttribute("admin", admin);
        model.addAttribute("sellers", all);

        return "admin/admin_seller_list";
    }

    /**
     * Deactivate seller (NO SERVICE).
     */
    @PostMapping("/admin/seller-deactivate/{userId}")
    public String deactivateSeller(
            @PathVariable Long userId,
            HttpSession session
    ) {

        AuthenticatedUser admin = getLoggedInAdmin(session);
        if (admin == null) {
            return "redirect:/admin/login";
        }

        sellerRepository.updateStatus(
                userId,
                SellerStatus.DEACTIVATED,
                admin.getEmail(),
                "Deactivated by admin"
        );

        return "redirect:/admin/seller-list";
    }
}
