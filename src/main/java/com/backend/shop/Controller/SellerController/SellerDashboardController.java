package com.backend.shop.Controller.SellerController;

import com.backend.shop.DataTransferObject.Seller.SellerDashboardDTO;
import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Model.Session.AuthenticatedUser;
import com.backend.shop.Repository.SellerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller")
public class SellerDashboardController {

    private final SellerRepository sellerRepository;

    public SellerDashboardController(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    /**
     * Show seller dashboard for approved sellers.
     */
    @GetMapping("/dashboard")
    public String showSellerDashboard(HttpSession session, Model model) {

        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;

        // Load seller by userId from repository
        SellerModel seller = sellerRepository.findByUserId(loggedInUser.getUserId());

        // If seller record does not exist, or status is not APPROVED,
        // redirect back to customer dashboard
        if (seller == null || seller.getStatus() != SellerStatus.APPROVED) {
            return "redirect:/customer/dashboard";
        }

        // Build DTO for the view
        SellerDashboardDTO dto = new SellerDashboardDTO();
        dto.setUserId(loggedInUser.getUserId());
        dto.setUsername(loggedInUser.getUsername());
        dto.setEmail(loggedInUser.getEmail());

        dto.setShopName(seller.getShopName());
        dto.setShopDescription(seller.getShopDescription());
        dto.setShopLogoUrl(seller.getShopLogoUrl());
        dto.setBusinessRegistrationNumber(seller.getBusinessRegistrationNumber());
        dto.setBusinessAddress(seller.getBusinessAddress());
        dto.setContactNumber(seller.getContactNumber());

        SellerStatus status = seller.getStatus();
        dto.setSellerStatus(status != null ? status.name() : null);
        dto.setReviewComment(seller.getReviewComment());

        // Put DTO into model
        model.addAttribute("sellerDashboard", dto);

        // Must match templates/seller/seller_dashboard.html
        return "seller/seller_dashboard";
    }
}
