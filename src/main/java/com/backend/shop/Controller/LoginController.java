package com.backend.shop.Controller;

import com.backend.shop.DataTransferObject.LoginDTO;
import com.backend.shop.Model.AuthenticatedUser;
import com.backend.shop.Service.LoginService;
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
 * Controller acts only as an entry point (gateway) for login requests.
 * It does NOT contain business logic; all logic is delegated to LoginService.
 */
@Controller
public class LoginController {

    private final LoginService loginService;

    // Constructor injection
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Display the login page.
     * If a protected page redirects the user here, show a message.
     */
    @GetMapping("/login")
    public String showLoginForm(
            Model model,
            @RequestParam(value = "needLogin", required = false) String needLogin
    ) {
        // Provide an empty LoginDTO for form binding
        model.addAttribute("loginDTO", new LoginDTO());

        // Display a message if user was redirected from a protected page
        if ("true".equals(needLogin)) {
            model.addAttribute("infoMessage", "Please log in before accessing that page.");
        }

        return "login";
    }

    /**
     * Process login form submission.
     * Delegates authentication to LoginService.
     */
    @PostMapping("/login")
    public String processLogin(
            @Valid @ModelAttribute("loginDTO") LoginDTO loginDTO,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {
        // If validation fails, re-render the login form
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // Delegate login logic to LoginService
        AuthenticatedUser user = loginService.login(loginDTO);

        // If login failed, show error message
        if (user == null) {
            model.addAttribute("loginError", "Invalid email or password");
            return "login";
        }

        // Store logged-in user in session
        session.setAttribute("loggedInUser", user);

        // Add username to page model
        model.addAttribute("username", user.getUsername());

        // Redirect based on user role
        if ("ADMIN".equals(user.getRole())) {
            return "redirect:/admin/dashboard";
        } else if ("CUSTOMER".equals(user.getRole())) {
            return "redirect:/customer/dashboard";
        } else if ("SELLER".equals(user.getRole())) {
            return "redirect:/seller/dashboard";
        }

        // Fallback (should never happen)
        return "login_success";
    }

    /**
     * Logout by clearing session and redirect to login page.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clear all session data
        return "redirect:/login";
    }
}
