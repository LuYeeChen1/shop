package com.backend.shop.Controller;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.CustomerModel;
import com.backend.shop.Model.SellerModel;
import com.backend.shop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * RegisterController handles public user registration.
 * Only CUSTOMER and SELLER can register through this controller.
 * ADMIN accounts must be created internally (e.g. by the company or DB admin).
 */
@Controller
public class RegisterController {

    private final UserService userService;

    // Constructor injection
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Display the registration form.
     * The "role" parameter supports only CUSTOMER and SELLER.
     * If ADMIN is passed in, it will be downgraded to CUSTOMER for safety.
     */
    @GetMapping("/register")
    public String showRegisterForm(
            @RequestParam(value = "role", required = false, defaultValue = "CUSTOMER") String role,
            Model model
    ) {
        // Normalize role to uppercase
        role = role.toUpperCase();

        // ADMIN is not allowed to register via public form
        if ("ADMIN".equals(role)) {
            role = "CUSTOMER"; // force to CUSTOMER
        }

        // Provide empty DTO for form binding
        model.addAttribute("registerDTO", new RegisterDTO());
        model.addAttribute("role", role);

        return "register";
    }

    /**
     * Process registration submission.
     * Only CUSTOMER and SELLER paths are allowed.
     * ADMIN is never registered through this endpoint.
     */
    @PostMapping("/register")
    public String processRegister(
            @RequestParam("role") String role,
            @Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {
        // Normalize role to uppercase
        role = role.toUpperCase();

        // ADMIN is not allowed from front-end, treat as CUSTOMER or reject
        if ("ADMIN".equals(role)) {
            // uncomment to show error instead
             model.addAttribute("role", "CUSTOMER");
             model.addAttribute("registerError", "Admin accounts cannot be registered here.");
             return "register";
        }

        // Basic validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("role", role);
            return "register";
        }

        // Check if email is already used across all user tables
        if (userService.emailExists(registerDTO.getEmail())) {
            model.addAttribute("role", role);
            model.addAttribute("registerError", "This email is already registered.");
            return "register";
        }

        // Handle registration logic based on allowed roles
        switch (role) {

            case "SELLER":
                // Register as seller
                SellerModel seller = userService.registerSeller(registerDTO);
                if (seller == null) {
                    model.addAttribute("role", role);
                    model.addAttribute("registerError", "Failed to register seller.");
                    return "register";
                }
                session.setAttribute("registeredUser", seller);
                break;

            case "CUSTOMER":
            default:
                // Register as customer (default)
                CustomerModel customer = userService.registerCustomer(registerDTO);
                if (customer == null) {
                    model.addAttribute("role", "CUSTOMER");
                    model.addAttribute("registerError", "Failed to register customer.");
                    return "register";
                }
                session.setAttribute("registeredUser", customer);
                break;
        }

        // After successful registration, redirect to login page
        return "redirect:/login";
    }
}
