package com.backend.shop.Controller.SellerController;

import com.backend.shop.DataTransferObject.Seller.SellerApplicationDTO;
import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Model.Session.AuthenticatedUser;
import com.backend.shop.Repository.Seller.SellerRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

/**
 * Controller that handles seller application flow for a logged-in customer.
 */
@Controller
@RequestMapping("/seller")
public class SellerApplicationController {

    private final SellerRepository sellerRepository;

    public SellerApplicationController(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    /**
     * Show the seller application form.
     * Only accessible if a customer is logged in and has not applied yet.
     */
    @GetMapping("/apply")
    public String showApplyForm(HttpSession session, Model model) {

        // Read logged-in user from session
        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            // Not logged in -> redirect to login
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;

        // If seller record already exists for this user, redirect to customer dashboard
        if (sellerRepository.existsByUserId(loggedInUser.getUserId())) {
            return "redirect:/customer/dashboard";
        }

        // Prepare empty DTO for the form
        SellerApplicationDTO dto = new SellerApplicationDTO();
        model.addAttribute("sellerApplicationDTO", dto);

        // Must match templates/seller/apply_seller.html
        return "seller/apply_seller";
    }

    /**
     * Handle seller application submission.
     */
    @PostMapping("/apply")
    public String handleApply(
            HttpSession session,
            @Valid @ModelAttribute("sellerApplicationDTO") SellerApplicationDTO dto,
            BindingResult bindingResult,
            Model model
    ) {

        // Read logged-in user
        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser loggedInUser)) {
            return "redirect:/login";
        }

        // If validation fails, show form again with errors
        if (bindingResult.hasErrors()) {
            return "seller/apply_seller";
        }

        // Double check whether seller record already exists
        if (sellerRepository.existsByUserId(loggedInUser.getUserId())) {
            return "redirect:/customer/dashboard";
        }

        // Create SellerModel and fill from DTO + session user
        SellerModel seller = new SellerModel();
        seller.setUserId(loggedInUser.getUserId());
        seller.setEmail(loggedInUser.getEmail()); // link seller to user email
        seller.setShopName(dto.getShopName());
        seller.setShopDescription(dto.getShopDescription());
        seller.setShopLogoUrl(dto.getShopLogoUrl());
        seller.setBusinessRegistrationNumber(dto.getBusinessRegistrationNumber());
        seller.setBusinessAddress(dto.getBusinessAddress());
        seller.setContactNumber(dto.getContactNumber());

        // Initial status: PENDING
        seller.setStatus(SellerStatus.PENDING);

        // Record applied time
        seller.setAppliedAt(LocalDateTime.now());

        // Save to database via repository (no service layer)
        sellerRepository.save(seller);

        // After application, go back to customer dashboard
        return "redirect:/customer/dashboard";
    }
}
