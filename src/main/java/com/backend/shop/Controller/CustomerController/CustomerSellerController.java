package com.backend.shop.Controller.CustomerController;

import com.backend.shop.Model.AuthenticatedUser;
import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Model.UserRole;
import com.backend.shop.Repository.SellerRepository;
import com.backend.shop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class CustomerSellerController {

    @Autowired
    private UserService userService;

    @Autowired
    private SellerRepository sellerRepository;

    /**
     * Helper: get logged-in user as AuthenticatedUser.
     */
    private AuthenticatedUser getLoggedInUser(HttpSession session) {
        Object obj = session.getAttribute("loggedInUser");
        if (obj instanceof AuthenticatedUser) {
            return (AuthenticatedUser) obj;
        }
        return null;
    }

    /**
     * Customer applies to become a seller.
     * Inserts or updates seller_table with PENDING status.
     */
    @PostMapping("/customer/apply-seller")
    public String applySeller(HttpSession session) {

        AuthenticatedUser user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        // Only CUSTOMER can apply
        if (!"CUSTOMER".equals(user.getRole())) {
            return "access-denied";
        }

        SellerModel seller = sellerRepository.findByUserId(user.getUserId());

        if (seller == null) {
            // First-time application
            seller = new SellerModel();
            seller.setUserId(user.getUserId());
            seller.setStatus(SellerStatus.PENDING);
            seller.setAppliedAt(LocalDateTime.now());
            sellerRepository.save(seller);
        } else {
            // Re-apply only if REJECTED or status null
            if (seller.getStatus() == SellerStatus.REJECTED || seller.getStatus() == null) {

                seller.setStatus(SellerStatus.PENDING);
                seller.setAppliedAt(LocalDateTime.now());
                seller.setReviewedAt(null);
                seller.setReviewedByAdmin(null);
                seller.setReviewComment(null);

                sellerRepository.updateStatus(
                        seller.getUserId(),
                        SellerStatus.PENDING,
                        null,
                        null
                );
            }
        }

        return "redirect:/customer/dashboard";
    }

    /**
     * Customer enters seller mode if approved.
     * Updates users.user_role + session role.
     */
    @PostMapping("/customer/enter-seller")
    public String enterSeller(HttpSession session) {

        AuthenticatedUser user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        SellerModel seller = sellerRepository.findByUserId(user.getUserId());

        // Must be approved to enter seller dashboard
        if (seller != null && seller.getStatus() == SellerStatus.APPROVED) {

            // Update role in DB
            userService.changeUserRole(user.getUserId(), UserRole.SELLER);

            // Update current session
            user.setRole(UserRole.SELLER.name());
            session.setAttribute("loggedInUser", user);

            return "redirect:/seller/dashboard";
        }

        return "access-denied";
    }
}
