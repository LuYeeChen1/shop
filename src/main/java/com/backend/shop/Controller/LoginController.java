package com.backend.shop.Controller;

import com.backend.shop.DataTransferObject.LoginDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final UserService userService;

    // Constructor injection
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    // Display login form
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    // Process login submission
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

        UserModel user = userService.authenticate(
                loginDTO.getEmail(),
                loginDTO.getPassword()
        );

        if (user == null) {
            // Authentication failed
            model.addAttribute("loginError", "Invalid email or password");
            return "login";
        }

        // Save user in session (simple login session)
        session.setAttribute("loggedInUser", user);

        model.addAttribute("username", user.getUsername());
        return "login_success";
    }

    // Optional: logout endpoint
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
