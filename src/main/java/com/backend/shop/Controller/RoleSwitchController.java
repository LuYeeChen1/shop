package com.backend.shop.Controller;

import com.backend.shop.Model.AuthenticatedUser;
import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;
import com.backend.shop.Repository.SellerRepository;
import com.backend.shop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RoleSwitchController {

    @Autowired
    private UserService userService;

    @Autowired
    private SellerRepository sellerRepository;

    /**
     * Switch from CUSTOMER to SELLER.
     * Only allowed if Seller application is APPROVED.
     */
    @PostMapping("/switch-to-seller")
    public String switchToSeller(HttpSession session) {

        AuthenticatedUser user = (AuthenticatedUser) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        // Check seller application status
        SellerModel seller = sellerRepository.findByUserId(user.getUserId());
        if (seller == null || seller.getStatus() != SellerStatus.APPROVED) {
            return "access-denied"; // cannot switch until approved
        }

        // Update role in DB
        UserModel updated = userService.changeUserRole(user.getUserId(), UserRole.SELLER);

        // Update session version
        user.setRole(UserRole.SELLER.name());
        session.setAttribute("loggedInUser", user);

        return "redirect:/seller/dashboard";
    }

    /**
     * Switch from SELLER back to CUSTOMER.
     * No approval required.
     */
    @PostMapping("/switch-to-customer")
    public String switchToCustomer(HttpSession session) {

        AuthenticatedUser user = (AuthenticatedUser) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        // Update DB
        userService.changeUserRole(user.getUserId(), UserRole.CUSTOMER);

        // Update session
        user.setRole(UserRole.CUSTOMER.name());
        session.setAttribute("loggedInUser", user);

        return "redirect:/customer/dashboard";
    }
}
