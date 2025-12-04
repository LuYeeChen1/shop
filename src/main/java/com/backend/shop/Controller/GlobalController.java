package com.backend.shop.Controller;

import com.backend.shop.Model.Admin.AdminModel;
import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Repository.Admin.AdminRepository;
import com.backend.shop.Repository.Seller.SellerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class GlobalController {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Make logged-in user available to all pages
     */
    @ModelAttribute("globalUser")
    public UserModel globalUser(HttpSession session) {
        return (UserModel) session.getAttribute("loggedInUser");
    }

    /**
     * Provide role for navbar: Customer / Seller / Admin
     */
    @ModelAttribute("navbarRole")
    public String navbarRole(HttpSession session) {

        // Admin login
        AdminModel admin = (AdminModel) session.getAttribute("loggedInAdmin");
        if (admin != null) {
            return "Admin";
        }

        // Customer login
        UserModel user = (UserModel) session.getAttribute("loggedInUser");
        if (user != null) {

            SellerModel seller = sellerRepository.findByUserId(user.getId());

            // Only APPROVED seller should be treated as Seller role
            if (seller != null && seller.getStatus() == SellerStatus.APPROVED) {
                return "Seller";
            }

            // PENDING / REJECTED / DEACTIVATED / null → treat as Customer
            return "Customer";
        }

        return null;
    }


    /**
     * Provide user ID for navbar
     * Admin: admin_id
     * Seller: user_id
     * Customer: user_id
     */
    @ModelAttribute("navbarUserId")
    public String navbarUserId(HttpSession session) {

        // Admin
        AdminModel admin = (AdminModel) session.getAttribute("loggedInAdmin");
        if (admin != null) {
            return String.valueOf(admin.getAdminId());
        }

        // Customer/Seller
        UserModel user = (UserModel) session.getAttribute("loggedInUser");
        if (user != null) {
            return String.valueOf(user.getId());  // seller id = user_id
        }

        return null;
    }

    /**
     * Provide username (only customer/seller)
     */
    @ModelAttribute("navbarUsername")
    public String navbarUsername(HttpSession session) {

        UserModel user = (UserModel) session.getAttribute("loggedInUser");
        if (user != null) {
            return user.getUsername();
        }

        return null;
    }
}
