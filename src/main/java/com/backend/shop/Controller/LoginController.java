package com.backend.shop.Controller;

import com.backend.shop.DataTransferObject.LoginDTO;
import com.backend.shop.Model.Session.AuthenticatedUser;
import com.backend.shop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
            @Valid @ModelAttribute("loginDTO") LoginDTO loginDTO,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // Authenticate and get a safe session object
        AuthenticatedUser authUser =
                userService.authenticate(loginDTO.getEmail(), loginDTO.getPassword());

        if (authUser == null) {
            model.addAttribute("loginError", "Invalid email or password");
            return "login";
        }

        // Store only AuthenticatedUser in session
        session.setAttribute("loggedInUser", authUser);

        // Redirect based on role
        String role = authUser.getRole();
        if ("ADMIN".equals(role)) {
            return "redirect:/admin/dashboard";
        } else if ("AGENT".equals(role)) {
            return "redirect:/agent/dashboard";
        } else if ("SELLER".equals(role)) {
            return "redirect:/seller/dashboard";
        } else {
            // Default: CUSTOMER or any other role
            return "redirect:/customer/dashboard";
        }
    }
}
