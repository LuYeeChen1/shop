package com.backend.shop.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogoutController {

    /**
     * Clears the session and logs the user out.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Remove all session data
        session.invalidate();

        // Redirect to login page
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logoutPost(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
