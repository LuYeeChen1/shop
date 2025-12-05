package com.backend.shop.Controller.CustomerController;

import com.backend.shop.DataTransferObject.ApplySellerDTO;
import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Model.Session.AuthenticatedUser;
import com.backend.shop.Repository.Seller.SellerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/customer/seller")
public class CustomerApplySellerController {

    private final SellerRepository sellerRepository;

    public CustomerApplySellerController(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @GetMapping("/apply")
    public String showApplyForm(HttpSession session, Model model) {

        AuthenticatedUser loggedInUser =
                (AuthenticatedUser) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("applySellerDTO", new ApplySellerDTO());
        model.addAttribute("email", loggedInUser.getEmail());

        return "customer/apply_seller";
    }

    @PostMapping("/apply")
    public String processApply(
            @ModelAttribute("applySellerDTO") ApplySellerDTO dto,
            HttpSession session,
            Model model
    ) {
        AuthenticatedUser loggedInUser =
                (AuthenticatedUser) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Build SellerModel from logged-in user + form data
        SellerModel seller = new SellerModel();
        seller.setUserId(loggedInUser.getUserId());
        seller.setEmail(loggedInUser.getEmail());

        seller.setShopName(dto.getShopName());
        seller.setShopDescription(dto.getShopDescription());
        seller.setShopLogoUrl(dto.getShopLogoUrl());
        seller.setBusinessRegistrationNumber(dto.getBusinessRegistrationNumber());
        seller.setBusinessAddress(dto.getBusinessAddress());
        seller.setContactNumber(dto.getContactNumber());

        seller.setStatus(SellerStatus.PENDING);
        seller.setAppliedAt(LocalDateTime.now());

        sellerRepository.save(seller);


        model.addAttribute("shopName", dto.getShopName());
        return "customer/apply_seller_success";
    }
}
