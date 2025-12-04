package com.backend.shop.Controller.CustomerController;

import com.backend.shop.DataTransferObject.Customer.CustomerDashboardDTO;
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
@RequestMapping("/customer")
public class CustomerDashboardController {

    private final SellerRepository sellerRepository;

    // Constructor injection (no Service, only Repository)
    public CustomerDashboardController(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    /**
     * Show the customer dashboard.
     * UI will change based on sellerStatus from DTO.
     */
    @GetMapping("/dashboard")
    public String showCustomerDashboard(HttpSession session, Model model) {

        // 1. Read logged-in user from session
        Object sessionUser = session.getAttribute("loggedInUser");

        if (!(sessionUser instanceof AuthenticatedUser)) {
            // Not logged in or wrong type -> redirect to login
            return "redirect:/login";
        }

        AuthenticatedUser customer = (AuthenticatedUser) sessionUser;

        // 2. Prepare DTO for the view
        CustomerDashboardDTO dashboardDTO = new CustomerDashboardDTO();
        dashboardDTO.setUserId(customer.getUserId());
        dashboardDTO.setUsername(customer.getUsername());
        dashboardDTO.setEmail(customer.getEmail());

        // Default values
        dashboardDTO.setSellerStatus("NONE");
        dashboardDTO.setSellerExists(false);
        dashboardDTO.setReviewComment(null);

        // 3. Load seller info by userId (no Service, directly use Repository)
        SellerModel seller = sellerRepository.findByUserId(customer.getUserId());

        if (seller != null) {
            dashboardDTO.setSellerExists(true);

            SellerStatus status = seller.getStatus();
            String statusValue = (status != null) ? status.name() : "NONE";
            dashboardDTO.setSellerStatus(statusValue);

            dashboardDTO.setReviewComment(seller.getReviewComment());
        }

        // 4. Put DTO into the model
        model.addAttribute("dashboard", dashboardDTO);

        // Must match src/main/resources/templates/customer/customer_dashboard.html
        return "customer/customer_dashboard";
    }
}
