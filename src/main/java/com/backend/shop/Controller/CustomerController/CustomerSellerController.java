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
     * Get logged-in user from session as AuthenticatedUser.
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
     * Creates or updates a SellerModel with PENDING status.
     */
    @PostMapping("/customer/apply-seller")
    public String applySeller(HttpSession session) {

        AuthenticatedUser user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        // Only CUSTOMER can apply to become a seller
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
            // Re-apply if previously NONE or REJECTED
            if (seller.getStatus() == null
                    || seller.getStatus() == SellerStatus.REJECTED) {

                seller.setStatus(SellerStatus.PENDING);
                seller.setAppliedAt(LocalDateTime.now());
                seller.setReviewedAt(null);
                seller.setReviewedByAdmin(null);
                seller.setReviewComment(null);
                sellerRepository.update(seller);
            }
            // If already PENDING or APPROVED, do nothing special
        }

        return "redirect:/customer/dashboard";
    }

    /**
     * Customer enters seller mode after being approved.
     * Changes users.user_role to SELLER and updates session.
     */
    @PostMapping("/customer/enter-seller")
    public String enterSeller(HttpSession session) {

        AuthenticatedUser user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        SellerModel seller = sellerRepository.findByUserId(user.getUserId());
        if (seller != null && seller.getStatus() == SellerStatus.APPROVED) {

            // Update role in users table
            userService.changeUserRole(user.getUserId(), UserRole.SELLER);

            // Update role in session
            user.setRole(UserRole.SELLER.name());
            session.setAttribute("loggedInUser", user);

            return "redirect:/seller/dashboard";
        }

        return "access-denied";
    }
}
