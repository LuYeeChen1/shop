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

@Controller
@RequestMapping("/seller")
public class SellerApplicationController {

    private final SellerRepository sellerRepository;

    public SellerApplicationController(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    /**
     * Show the seller application form.
     */
    @GetMapping("/apply")
    public String showApplyForm(HttpSession session, Model model) {

        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;

        // Optional: if seller record already exists, redirect back to dashboard
        if (sellerRepository.existsByUserId(loggedInUser.getUserId())) {
            return "redirect:/customer/dashboard";
        }

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

        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;

        if (bindingResult.hasErrors()) {
            // Validation failed, show form again with errors
            return "seller/apply_seller";
        }

        // Create SellerModel and fill from DTO
        SellerModel seller = new SellerModel();
        seller.setUserId(loggedInUser.getUserId());
        seller.setShopName(dto.getShopName());
        seller.setShopDescription(dto.getShopDescription());
        seller.setShopLogoUrl(dto.getShopLogoUrl());
        seller.setBusinessRegistrationNumber(dto.getBusinessRegistrationNumber());
        seller.setBusinessAddress(dto.getBusinessAddress());
        seller.setContactNumber(dto.getContactNumber());

        // Initial status: PENDING
        seller.setStatus(SellerStatus.PENDING);

        // Set applied time
        seller.setAppliedAt(LocalDateTime.now());

        // Save to database via repository (no service)
        sellerRepository.save(seller);

        // After application, go back to customer dashboard
        return "redirect:/customer/dashboard";
    }
}
