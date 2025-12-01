package com.backend.shop.Controller;

import com.backend.shop.Model.AuthenticatedUser;
import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Repository.SellerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;

public class BaseController {

    @Autowired
    private SellerRepository sellerRepository;

    @ModelAttribute("currentUser")
    public AuthenticatedUser addLoggedInUser(HttpSession session) {
        Object obj = session.getAttribute("loggedInUser");
        if (obj instanceof AuthenticatedUser auth) {
            return auth;
        }
        return null;
    }

    @ModelAttribute("seller")
    public SellerModel addSellerInfo(HttpSession session) {
        Object obj = session.getAttribute("loggedInUser");
        if (obj instanceof AuthenticatedUser auth) {
            return sellerRepository.findByUserId(auth.getUserId());
        }
        return null;
    }

    /**
     * Add total pending seller applications for ADMIN navbar.
     */
    @ModelAttribute("pendingSellerCount")
    public Integer pendingSellerCount() {
        return sellerRepository.countPending();
    }

}
