package com.backend.shop.Controller.SellerController;

import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Repository.SellerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller")
public class SellerDashboardController {

    @Autowired
    private SellerRepository sellerRepository;

    /**
     * Seller deactivates their own seller account.
     */
    @PostMapping("/deactivate")
    public String deactivateOwnSellerAccount(HttpSession session) {

        UserModel user = (UserModel) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        // No admin email when seller deactivates by themselves
        sellerRepository.updateStatus(
                user.getId(),
                SellerStatus.DEACTIVATED,
                null,
                "Seller requested to deactivate their account."
        );

        // After deactivation, you can redirect to customer dashboard
        return "redirect:/customer/dashboard";
    }
}
