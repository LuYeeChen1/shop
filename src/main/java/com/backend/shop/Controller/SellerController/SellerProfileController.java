package com.backend.shop.Controller.SellerController;

import com.backend.shop.DataTransferObject.Seller.SellerProfileUpdateDTO;
import com.backend.shop.Model.Seller.SellerModel;
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

@Controller
@RequestMapping("/seller")
public class SellerProfileController {

    private final SellerRepository sellerRepository;

    public SellerProfileController(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    /**
     * Show edit profile form for seller.
     */
    @GetMapping("/profile/edit")
    public String showEditProfileForm(HttpSession session, Model model) {

        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;

        // Load seller record
        SellerModel seller = sellerRepository.findByUserId(loggedInUser.getUserId());
        if (seller == null) {
            // If no seller record, redirect back to customer dashboard
            return "redirect:/customer/dashboard";
        }

        // Build DTO from existing seller data
        SellerProfileUpdateDTO dto = new SellerProfileUpdateDTO();
        dto.setShopName(seller.getShopName());
        dto.setShopDescription(seller.getShopDescription());
        dto.setShopLogoUrl(seller.getShopLogoUrl());
        dto.setBusinessRegistrationNumber(seller.getBusinessRegistrationNumber());
        dto.setBusinessAddress(seller.getBusinessAddress());
        dto.setContactNumber(seller.getContactNumber());

        model.addAttribute("sellerProfileUpdateDTO", dto);

        // Must match templates/seller/edit_profile.html
        return "seller/edit_profile";
    }

    /**
     * Handle seller profile update submission.
     */
    @PostMapping("/profile/edit")
    public String handleEditProfile(
            HttpSession session,
            @Valid @ModelAttribute("sellerProfileUpdateDTO") SellerProfileUpdateDTO dto,
            BindingResult bindingResult,
            Model model
    ) {

        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;

        if (bindingResult.hasErrors()) {
            // Validation failed, show the form again
            return "seller/edit_profile";
        }

        // Build SellerModel with updated fields
        SellerModel sellerToUpdate = new SellerModel();
        sellerToUpdate.setUserId(loggedInUser.getUserId());
        sellerToUpdate.setShopName(dto.getShopName());
        sellerToUpdate.setShopDescription(dto.getShopDescription());
        sellerToUpdate.setShopLogoUrl(dto.getShopLogoUrl());
        sellerToUpdate.setBusinessRegistrationNumber(dto.getBusinessRegistrationNumber());
        sellerToUpdate.setBusinessAddress(dto.getBusinessAddress());
        sellerToUpdate.setContactNumber(dto.getContactNumber());

        // Call repository to update profile (no service)
        sellerRepository.updateProfile(sellerToUpdate);

        // After update, redirect back to seller dashboard
        return "redirect:/seller/dashboard";
    }
}
